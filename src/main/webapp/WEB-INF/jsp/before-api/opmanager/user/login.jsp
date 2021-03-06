<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head><meta charset="UTF-8"/>
    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/webapp/content/common.css">
    <title>회원등록</title>
</head>

<body>
<%--@elvariable id="user" type="User"--%>
<form:form modelAttribute="user" method="post" id="target" >
    <p>
    <span>
        아이디 <form:input path="loginId" maxlength="12" readonly=""/> <br>
        비밀번호 <form:password path="password" maxlength="8" readonly=""/> <br>
    <input type="submit" class="submit" value="로그인"  formaction="/opmanager/user/login">
    </span>
    </p>
</form:form>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

	//최종 제출시 이벤트
	$("#target").on("submit", function() {

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
	});
</script>
</body>
</html>