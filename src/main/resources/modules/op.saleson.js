/**
 * SalesOn3 API Client
 *
 * @Date 2019.10.07.
 * @Author skc@onlinepowers.com
 * @type {{exception: Saleson.exception, redirect: Saleson.redirect, init: Saleson.init, debug: Saleson.debug, const: {SAVED_LOGIN_ID: string, CATEGORY: string, TOKEN: string}, notFoundException: Saleson.notFoundException, log: Saleson.log, callbackAlert: Saleson.callbackAlert, error: Saleson.error, confirm: Saleson.confirm, isLogin: (function(): (*|boolean)), authenticationException: Saleson.authenticationException, pages: {LOGIN: string, INDEX: string, isAllowAnonymous: (function(): boolean)}, handleException: Saleson.handleException, requestContext: {}, alert: Saleson.alert, config: {cdnDomain: string, apiDomain: string, HMACSecretKey: string}}}
 */
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
    },

    init: function () {

        $s = Saleson;

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
    }
};

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
    getAuthToken: function (req) {      // 로그인창에서 호출, req에 id, pw포함됨
        if (req == null) {
            return;
        }

        $s.axios.post(`http://localhost:8080/api/user/login`, req)       //req = vm.loginRequest 로 넘김 ,,

            .then(function (response) {                                 //컨트롤러로 보낸 결과 (토큰) 를 받음
                console.log(req);   //로그인시 입력한 값 담김.
                console.log("토큰값=======" + response);
                localStorage.setItem("token",  response);

            })
            .catch(function (error) {
               console.log("로그인실패");
            });
    }
};
