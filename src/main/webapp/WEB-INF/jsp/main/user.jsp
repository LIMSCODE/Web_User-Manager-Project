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

    <sec:authorize access="isAuthenticated()">
    <!--인증이 되었으면 나타남-->
    <span>
         <span><sec:authentication property="name"/>님 안녕하세요</span>   <br><br><br>        <!--Authentication 객체의 property를 반환-->
         <a class="btn" href="/user/password-check"  >정보수정</a>      <br><br><br>
         <a class="btn" href="/user/logout" >로그아웃</a>
    </span>
    </sec:authorize>

    <sec:authorize access="!isAuthenticated()">
    <span>
         <a class="btn" href="/user/login" >로그인하기</a>
         <a class="btn" href="/user/create" >회원가입</a>
    </span>
    </sec:authorize>

</div>
</body>

</html>