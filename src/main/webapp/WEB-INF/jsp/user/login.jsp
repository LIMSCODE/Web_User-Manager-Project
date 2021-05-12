<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>회원등록</title>
    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/webapp/content/common.css">
</head>

<body>
<%--@elvariable id="user" type="com.onlinepowers.springmybatis.user.User"--%>
<form:form modelAttribute="user" method="post" id="target">
    <p>
    <span>
         아이디 <form:input path="loginId" maxlength="12" /> <br>
         비밀번호 <form:password path="password" maxlength="8"/> <br>
    <input type="submit" class="submit" value="로그인"  formaction="/api/user/login">
    </span>
    </p>
</form:form>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js">
	//최종 제출시 이벤트
	$("#target").on("submit", function(e) {

		e.preventDefault();

		let $loginId = $("#loginId");
		let $password = $("#password");

		if ($loginId.val() == "") {
			alert("아이디 입력해주세요");
			$loginId.focus();
			return false;
		}

		if ($password.val() == "") {
			alert("비밀번호 입력해주세요");
			$password.focus();
			return false;
		}

		var loginFormData = new FormData($("#target")[0]);

		axios.request({
			url: "/api/user/login", 11111111111
			method: "post",
			data: loginFormData,
			datatype: 'text',       //생성된 토큰을 받는다.
			processData: false,
			contentType: false,

		}) .then (token => {
			console.log(token);
			localStorage.setItem('wtw-token', token);
			//var expireDay = 24 * 60 * 60 * 1000; //1일
			//document.cookie = "X-AUTH-TOKEN=" + token + expireDay +"; path=/";  //쿠키에 토큰 저장
			window.location.href = "/"
		}) .catch(err => this.setState({ error: err.toString() }));

	});
</script>

</body>

</html>