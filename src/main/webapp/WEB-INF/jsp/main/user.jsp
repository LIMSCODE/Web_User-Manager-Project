<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/webapp/content/common.css">
</head>

<body>
<div class="">
    <br><br><br><br>

    <sec:authorize access="hasAnyRole('ROLE_USER')">        <!--유저일때-->
    <span>
         <span><sec:authentication property="name"/>님 안녕하세요</span>   <br><br><br>        <!--Authentication 객체의 property를 반환-->
         <a class="btn" href="/user/password-check"  >정보수정</a>      <br><br><br>
         <a class="btn" onclick="logout();">로그아웃</a>
    </span>
    </sec:authorize>

    <sec:authorize access="!isAuthenticated()">
    <span>
         <a class="btn" href="/user/login" >로그인하기</a>
         <a class="btn" href="/user/create" >회원가입</a>
    </span>
    </sec:authorize>

</div>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

    function logout() {

        $.ajax({
            url : "/api/user/logout",
            type : "get",
            datatype : 'text',       //생성된 토큰을 받는다.
            processData : false,
            contentType : false,

            success : function(data) {
                document.cookie = "X-AUTH-TOKEN =" + ';';  //쿠키 공백으로 넣음
                window.location.href = "/"
            },
            error : function() {
                alert("실패");
            }
        });
    }

</script>

</body>

</html>