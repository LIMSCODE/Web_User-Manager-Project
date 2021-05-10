<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<div class="">
<br><br><br><br><br>
    <sec:authorize access="hasAnyRole('ROLE_OPMANAGER')">
    <span>[관리자 페이지] </span>
    <span><sec:authentication property="name"/> 님 안녕하세요</span>   <br><br><br>
    <a class="btn" href="/opmanager/logout" onclick="logout();">로그아웃</a>   <br><br><br>
    </sec:authorize>
</div>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

    function logout() {

        $.ajax({
            url : "/opmanager/logout",
            type : "get",
            datatype: 'text',
            processData: false,
            contentType: false,

            success : function(data) {
                alert("로그아웃 성공");
                document.cookie = "X-AUTH-TOKEN =" + ';';  //쿠키 공백으로 넣음
                window.location.href = "/"
            },

            error : function() {
                alert("실패");
            }
        });
    }
</script>
