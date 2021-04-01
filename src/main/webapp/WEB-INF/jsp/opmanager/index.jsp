<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>관리자 메인페이지</title>
    <style>
        .btn{
            wid 20%; background-color: darkslategrey; color: white;
            padding: 14px 20px; margin: 8px 0; border: none;
            border-radius: 4px; cursor: pointer; text-decoration: none;
        }
    </style>
</head>

<body>

<div class="">
    <br><br><br><br>
    <c:if test="${loginUser != null}">
        <main id="right">메인페이지</main>
    </c:if>
</div>

</body>

</html>