<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="ko">

<c:if test="${user != null and user.paginationInfo.totalRecordCount > 0}">

<div fragment="pagination"
     with="info=${user.paginationInfo}" aria-label="Page navigation"
     class="text-center">

    <ul class="pagination">
        <c:if test="${user.paginationInfo.hasPreviousPage == true}">
            <li onclick="movePage([[ ${pageContext.request.requestURI} ]], [[ ${user.makeQueryString(1)} ]])">
            <a href="javascript:void(0)" class="btn_arr first" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
            </li>
        </c:if>
        <c:if test="${user.paginationInfo.hasPreviousPage == true}">
            <li onclick="movePage([[ ${pageContext.request.requestURI} ]], [[ ${user.makeQueryString(info.firstPage - 1)} ]])">
            <a href="javascript:void(0)" class="btn_arr prev" aria-label="Previous"><span aria-hidden="true">&lsaquo;</span></a>
            </li>
        </c:if>

        <c:forEach begin="${user.paginationInfo.firstPage}" end="${user.lastPage}" var="idx">
            <li class="${idx == user.currentPageNo} ? 'active'">
            <a href="javascript:void(0)" text="${idx}"  class="on"
               onclick="movePage([[ ${pageContext.request.requestURI}  ]], [[ ${user.makeQueryString(idx)} ]])">&andslope; </a>
            </li>
        </c:forEach>

        <c:if test="${user.paginationInfo.hasNextPage == true}">
            <li onclick="movePage([[ ${pageContext.request.requestURI} ]], [[ ${user.makeQueryString(info.lastPage + 1)} ]])">
            <a href="javascript:void(0)" class="btn_arr next"
               aria-label="Next"><span aria-hidden="true">&rsaquo;</span></a>
            </li>
        </c:if>
        <c:if test="${user.paginationInfo.hasNextPage == true}">
            <li onclick="movePage([[ ${pageContext.request.requestURI} ]], [[ ${user.makeQueryString(info.totalPageCount)} ]])">
            <a href="javascript:void(0)" class="btn_arr last" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
            </li>
        </c:if>
    </ul>
</div>
</c:if>
</html>