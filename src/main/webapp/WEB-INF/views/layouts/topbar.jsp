<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="">
<br><br><br><br><br>
<span>[관리자 페이지] </span>
    <div sec:authorize="isAuthenticated()">
    <span><sec:authentication property="name"/> 님 안녕하세요</span>   <br><br><br>
    <a class="btn" href="/opmanager/logout" >로그아웃</a>   <br><br><br>
    </div>
</div>
