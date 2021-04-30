<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<title>관리자 메인페이지</title>
<body>
<div class="">
    <br><br><br><br>
    <c:if test="${loginUser != null}">
    <div sec:authorize="isAuthenticated()">
        <main id="right">메인페이지</main>
    </div>
    </c:if>
</div>
</body>
