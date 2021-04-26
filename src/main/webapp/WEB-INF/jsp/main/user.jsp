<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

    <c:if test="${loginUser != null}">
    <div sec:authorize="isAuthenticated()">     <!--인증이 되었으면 나타남-->
    <span>
         <span sec:authentication="name">${loginUser.name} 님 안녕하세요</span>   <br><br><br>        <!--Authentication 객체의 property를 반환-->
         <a class="btn" href="/user/password-check"  >정보수정</a>      <br><br><br>
         <a class="btn" href="/user/logout" >로그아웃</a>
    </span>
    </div>
    </c:if>

    <c:if test="${loginUser == null}">
    <span>
         <a class="btn" href="/user/login" >로그인하기</a>
         <a class="btn" href="/user/create" >회원가입</a>
    </span>
    </c:if>

</div>
</body>

</html>