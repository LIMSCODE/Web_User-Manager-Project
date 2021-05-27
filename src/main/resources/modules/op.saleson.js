
//ss
var $s = Saleson = {
  config: {
    apiDomain: API_DOMAIN,
    cdnDomain: CDN_DOMAIN,
    virtualDomain: VIRTUAL_DOMAIN,
    HMACSecretKey: 'b18n9591c9y220uw5o82c8vb',
  },
  const: {
    SALESON_ID: 'saleson_id',
    TOKEN: 'token',
    TOKEN_STATUS: 'token_status',
    TOKEN_TYPE: 'token_type',
    CATEGORY: 'category',
    CATEGORY_UPDATED_DATE: 'category_updated_date',
    SAVED_LOGIN_ID: 'saved_login_id',
    CAMPAIGN_CODE: 'campaign_code',
    VISIT: 'visit',
    VISIT_EXPIRE_DATE: 'visit_expire_date',
    LATELY_SEARCH: 'lately_search',
    BUY_ORDER: 'buy_order'
  },

  requestContext: {},
  pages: {
    INDEX: "/",
    LOGIN: "/users/login.html",
    FIND_ID_PW: "/users/find-idpw.html",
    JOIN: "/users/join.html",
    JOIN_COMPLETE: "/users/join-complete.html",
    SLEEP_USER: "/users/sleep-user.html",
    CHANGE_PASSWORD: "/users/change-password.html",
    MYPAGE_ORDER: "/mypage/order.html",

    /**
     * 비회원 접속이 가능한 페이지인가?
     * @returns {boolean}
     */
    isAllowAnonymous: function () {
      var allowAnonymous = [
        $s.pages.LOGIN,
        $s.pages.FIND_ID_PW,
        $s.pages.JOIN,
        $s.pages.JOIN_COMPLETE,
        $s.pages.SLEEP_USER,
        $s.pages.CHANGE_PASSWORD
      ];
      var requestUri = $s.requestContext.requestUri;
      var isMatched = false;

      for (var i = 0; i < allowAnonymous.length; i++) {
        if (allowAnonymous[i] == requestUri) {
          isMatched = true;
          break;
        }
      }
      return isMatched;
    },
  },

  init: function (options) {

    $s = Saleson;
    $api = $s.api;
    $c = $s.const;
    $p = $s.pages;

    // check
    if (axios == 'undifined' || typeof axios !== 'function') {
      alert('Axios is not loaded.');
      return;
    }
    axios.defaults.baseURL = this.config.apiDomain;
    axios.defaults.headers.get['Content-Type'] = 'application/x-www-form-urlencoded';
    axios.defaults.headers.put['Content-Type'] = 'application/json;charset=utf-8';
    axios.defaults.headers.patch['Content-Type'] = 'application/json;charset=utf-8';
    axios.defaults.headers.patch['Access-Control-Allow-Origin'] = '*';
    axios.defaults.headers.delete['Content-Type'] = 'application/json;charset=utf-8';
    axios.defaults.headers.delete['Access-Control-Allow-Origin'] = '*';
    axios.defaults.headers.post['Content-Type'] = 'application/json;charset=utf-8';
    axios.defaults.headers.post['Access-Control-Allow-Origin'] = '*';

    this.axios = axios;
    this.requestContext = this.core.parseUrl(location.href);

    $s.debug(this.requestContext);

    // 인증설정
    try {

      var isLoginPage = false;
      var isGuestLoginPage = false;

      if (typeof options != 'undefined') {
        try {
          isLoginPage = options.loginPage;
          isGuestLoginPage = options.guestLoginPage;
        } catch (e) {
        }
      }

      $s.setCampaignCode();

      $s.api.salesonId(function () {
      }, function (error) {
        $s.error(error);
      })

      $s.core.authenticationFilter(function () {

        if (!$s.pages.isAllowAnonymous()) {

          var alertFlag;

          if (isGuestLoginPage) {
            alertFlag = !$s.isGuestLogin();
          } else {
            alertFlag = isLoginPage;
          }

          if (alertFlag) {
            $s.authenticationException('로그인 후 이용이 가능합니다.');
          }
        }
      });

      $s.ga.init();
      $s.ev.init();
    } catch (e) {
      this.handleException(e);
      return;
    }
  },

  cleanToken: function () {
    sessionStorage.removeItem($s.const.TOKEN);
    sessionStorage.removeItem($s.const.TOKEN_TYPE);
    sessionStorage.removeItem($s.const.TOKEN_STATUS);
  },

  setToken: function (token, status, type) {
    $s.core.setSession($s.const.TOKEN, token);
    $s.core.setSession($s.const.TOKEN_STATUS, status);
    $s.core.setSession($s.const.TOKEN_TYPE, type);
  }
}

//api

  $s.api = {
    domain: $s.config.apiDomain,

    /**
     * 인증 토큰 발행
     *
     * URI: /api/auth/token
     * @HeaderParam Hmac
     * @JsonPath loginType
     * @JsonPath loginId
     * @JsonPath password
     */
    getAuthToken: function (req, successHandler, failureHandler) {
      if (req == null) {
        return;
      }

      var hmacMessage = JSON.stringify(req);
      $s.debug(hmacMessage);

      var hashInBase64 = $s.getHashInBase64(hmacMessage);
      $s.debug("HMAC" + hashInBase64);

      var url = '/api/auth/token';
      try {
        url += '?uid=' + $s.ev.getUid();
      } catch (e) {
      }

      $s.axios
          .post(url, req, {
            headers: {Hmac: hashInBase64}
          })
          .then(function (response) {
            $s.setToken(response.data.token, response.status, 'USER');

            if ($s.core.isFunction(successHandler)) {
              successHandler();
            }
          })
          .catch(function (error) {
            if ($s.core.isFunction(failureHandler)) {
              failureHandler(error);
            } else {
              $s.api.handleApiExeption(error, failureHandler);
            }
          });
    }
  }

//코어

$s.core = {
  const: {
    TOKEN: 'token'
  },
  authenticationContext: {},
  authenticationFilter: function (guestHandler, authHandler) {
    // 인증정보 처리
    if ($s.isLogin()) {
      this.authenticationContext.accessToken = $s.core.getSession(this.const.TOKEN);
      //$s.log("authContext : " + JSON.stringify(this.authenticationContext));

      if (this.isFunction(authHandler)) {
        authHandler();
      }

    } else {
      if (this.isFunction(guestHandler)) {
        guestHandler();
      }

    }
  },
  isAuthenticated: function () {
    if (this.getSession($s.const.TOKEN) == null || this.getSession($s.const.TOKEN_STATUS) != 200) {
      //$s.debug('token is not set');
      return false;
    } else {
      //$s.debug('token is ' + this.getSession($s.const.TOKEN));
      return true;
    }
  },
  isFunction: function (func) {
    return func != null && typeof func === 'function' ? true : false;
  },
  getSession: function (key) {
    return sessionStorage.getItem(key);
  },
  setSession: function (key, value) {
    sessionStorage.setItem(key, value);
  },
  removeSession: function (key) {
    sessionStorage.removeItem(key);
  },
  getData: function (key) {
    return localStorage.getItem(key);
  },
  setData: function (key, value) {
    localStorage.setItem(key, value);
  },
  removeData: function (key) {
    localStorage.removeItem(key);
  },
  parseUrl: function (url) {
    var parser = document.createElement("a");
    parser.href = url;

    // IE 8 and 9 dont load the attributes "protocol" and "host" in case the source URL
    // is just a pathname, that is, "/example" and not "http://domain.com/example".
    parser.href = parser.href;

    // IE 7 and 6 wont load "protocol" and "host" even with the above workaround,
    // so we take the protocol/host from window.location and place them manually
    if (parser.host === "") {
      var newProtocolAndHost = window.location.protocol + "//" + window.location.host;
      if (url.charAt(1) === "/") {
        parser.href = newProtocolAndHost + url;
      } else {
        // the regex gets everything up to the last "/"
        // /path/takesEverythingUpToAndIncludingTheLastForwardSlash/thisIsIgnored
        // "/" is inserted before because IE takes it of from pathname
        var currentFolder = ("/" + parser.pathname).match(/.*\//)[0];
        parser.href = newProtocolAndHost + currentFolder + url;
      }
    }

    // copies all the properties to this object
    var properties = ['host', 'hostname', 'hash', 'href', 'port', 'protocol', 'search'];
    for (var i = 0, n = properties.length; i < n; i++) {
      this[properties[i]] = parser[properties[i]];
    }

    // pathname is special because IE takes the "/" of the starting of pathname
    this.pathname = (parser.pathname.charAt(0) !== "/" ? "/" : "") + parser.pathname;

    // requestUri
    this.requestUri = this.pathname;
    this.requestFullUri = this.pathname + this.search;

    return this;
  },

  getParameter: function (name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    var results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  },

  salesonId: function () {

  }
};
