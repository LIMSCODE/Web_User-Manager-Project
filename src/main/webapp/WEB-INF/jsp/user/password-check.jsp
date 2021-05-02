<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>회원등록</title>
    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/webapp/content/common.css">
</head>

<body>

<sec:authorize access="isAuthenticated()">
<%--@elvariable id="user" type="com.onlinepowers.springmybatis.user.User"--%>
<form:form modelAttribute="user" method="post" id="target">
    <span>
        <form:hidden path="id" value="${id}"/>
        비밀번호<form:password path="password" maxlength="8"/> <br>
    </span>
    <span>
    <input type="submit" class="submit" value="비밀번호 확인" formaction="/user/password-check">
    </span>
</form:form>

<br><br>
<td><a class="btn" href="/">메인화면</a> </td>
</sec:authorize>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

	//최종 제출시 이벤트
	$("#target").on("submit", function(e) {

        e.preventDefault();

        let $password = $("#password");

		if ($password.val() == "") {
			alert("비밀번호 입력해주세요");
			$password.focus();
			return false;
		}

        var passwordFormData = new FormData($("#target")[0]);

        $.ajax({
            url : "/user/password-check",
            type : "post",
            data : passwordFormData,
            datatype: 'json',
            processData: false,
            contentType: false,
            //contentType: "application/json",
            //datatype: "string",
            success : function(data) {
                alert("비밀번호 일치 확인");
                window.location.href = "/user/edit/" + data.id     //json으로 넘긴 값
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