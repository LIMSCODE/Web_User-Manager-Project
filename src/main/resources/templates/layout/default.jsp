<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<header>
    <style>
        header {
            background: lightgray;
            height: 130px;
        }

        sidebar {
            background : #f2f4f7;
            float : left;
            width : 20%;
            height : 1000px;
            position : relative;
            top : 30px;
        }
        contentbar {
            float : right;
            width : 78%;
            height : 1000px;
            position : relative;
            top : 30px;
        }
    </style>
</header>

<body id="page-top">
<div id="wrapper">
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- 상단바 -->
        <header>
            <jsp:include page="top.jsp" flush="false"/>
        </header>

        <!-- 사이드바 -->
        <sidebar>
            <jsp:include page="top.jsp" flush="false"/>
        </sidebar>

        <!-- 본문 -->
        <contentbar>
            <jsp:include page="top.jsp" flush="false"/>
        </contentbar>
    </div>
</div>

</body>
</html>