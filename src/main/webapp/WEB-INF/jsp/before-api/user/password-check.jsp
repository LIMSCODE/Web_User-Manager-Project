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
    <span>
        비밀번호<form:password path="password" maxlength="8"/> <br>
    </span>
    <span>
    <input type="submit" class="submit" value="비밀번호 확인" formaction="/user/password-check">
    </span>
</form:form>

<br><br>
<td><a class="btn" href="/">메인화면</a> </td>


<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

	//최종 제출시 이벤트
	$("#target").on("submit", function() {
		let $password = $("#password");

		if ($password.val() == "") {
			alert("비밀번호 입력해주세요");
			$password.focus();
			return false;
		}
	});

</script>
</body>

</html>