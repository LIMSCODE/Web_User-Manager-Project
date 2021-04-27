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
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <span>
         아이디 <form:input path="loginId" maxlength="12" /> <br>
         비밀번호 <form:password path="password" maxlength="8"/> <br>
    <input type="submit" class="submit" value="로그인"  formaction="/user/login">
    </span>
    </p>
</form:form>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

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

		var loginForm = $("#target");
		var loginFormData = new FormData(loginForm[0]);

		$.ajax({
			url : "/user/login",
			type : "post",
			data : loginFormData,
			datatype: 'json',
			processData: false,
			contentType: false,
			//contentType: "application/json",
			//datatype: "string",
			success : function(data) {
				alert("로그인");
                window.location.href = "/"
			},
			error : function() {
				alert(this.url);
				alert("실패");
			}
		});
	});
</script>

</body>

</html>