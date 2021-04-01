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
    <style>
        input[type=text],input[type=password] {
            width: 45%; /*입력 칸 (input field) 의 폭을 지정하기 위해, 폭 속성 (width property) 를 사용하였습니다.*/
            padding: 12px 20px; margin: 8px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px;
            box-sizing: border-box; }

        input[type=submit] {
            width: 50%; background-color: darkslategrey; color: white;
            padding: 14px 20px; margin: 8px 0; border: none;
            border-radius: 4px; cursor: pointer; text-decoration: none;
        }
        input[type=button], button[type=button] {
            width: 20%; background-color: darkslategrey; color: white;
            padding: 14px 20px; margin: 8px 0; border: none;
            border-radius: 4px; cursor: pointer; text-decoration: none;
        }
        .btn{
            width: 20%; background-color: darkslategrey; color: white;
            padding: 14px 20px; margin: 8px 0; border: none;
            border-radius: 4px; cursor: pointer; text-decoration: none;
        }
    </style>
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