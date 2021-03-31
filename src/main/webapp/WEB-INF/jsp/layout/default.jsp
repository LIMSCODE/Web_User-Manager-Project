<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
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
            <tiles:insertAttribute name="topbar" />
        </header>

        <!-- 사이드바 -->
        <sidebar>
            <tiles:insertAttribute name="sidebar" />
        </sidebar>

        <!-- 본문 -->
        <contentbar>
            <tiles:insertAttribute name="content" />
        </contentbar>

    </div>
</div>

</body>
</html>