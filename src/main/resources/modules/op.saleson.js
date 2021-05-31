var $s = Saleson = {

	config: {
		apiDomain: API_DOMAIN,
		cdnDomain: CDN_DOMAIN,
		virtualDomain: VIRTUAL_DOMAIN,
		HMACSecretKey: 'b18n9591c9y220uw5o82c8vb',
    },

    const: {
        TOKEN: 'token',
        TOKEN_STATUS: 'token_status',
        TOKEN_TYPE: 'token_type',
    },
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

        $s.axios
            .post(`http://localhost:8080/api/user/login`, req)       //req = vm.loginRequest 로 넘김 (id, pw)
            .then(function (response) {                              //컨트롤러에서 토큰 받음

                console.log(req);
                console.log("토큰값=======" + response);
                localStorage.setItem("token",  response);

            })
            .catch(function (error) {
               console.log("로그인실패");
            });
    }
};

