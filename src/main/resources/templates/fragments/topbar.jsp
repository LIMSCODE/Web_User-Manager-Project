<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">

<div class="">
<br>
<span>[관리자 페이지]   </span>
    <span>${loginUser.name} 님 안녕하세요</span>   <br><br><br>
    <a class="btn" href="/opmanager/logout" >로그아웃</a>   <br><br><br>
</div>

</html>