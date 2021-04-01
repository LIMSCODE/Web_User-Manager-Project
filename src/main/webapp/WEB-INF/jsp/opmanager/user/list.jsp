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
        ul {
            list-style:none;
        }
        li {
            float: left;
        }
        table.type09 {
            border-collapse: collapse;
            text-align: left;
            line-height: 1.5;
        }
        table.type09 thead th {
            padding: 10px;
            font-weight: bold;
            vertical-align: top;
            color: #369;
            border-bottom: 3px solid #036;
        }
        table.type09 tbody th {
            wid 150px;
            padding: 10px;
            font-weight: bold;
            vertical-align: top;
            border-bottom: 1px solid #ccc;
            background: #f3f6f7;
        }
        table.type09 td {
            wid 350px;
            padding: 10px;
            vertical-align: top;
            border-bottom: 1px solid #ccc;
        }

        input[type=submit]  {
            wid 5%; background-color: darkslategrey; color: white;
            padding: 10px 10px; margin: 5px 0; border: none;
            border-radius: 3px; cursor:
                pointer;
        }

        td button[type=submit] {
            wid50px; background-color: darkslategrey; color: white;
            padding: 10px 10px; margin: 5px 0; border: none;
            border-radius: 3px; cursor:
                pointer;
        }

        #edit {
            wid25px; background-color: darkslategrey; color: white;
            padding: 10px 10px; margin: 10px 0; border: none;
            border-radius: 3px; cursor: pointer; text-decoration:none;
            position: relative; top:10px;
        }

        #search {
            float:left;
        }

        button[type=submit] {
            wid100px; background-color: darkslategrey; color: white;
            padding: 10px 10px; margin: 5px 0; border: none;
            border-radius: 3px; cursor:
                pointer;
        }

        .reset {
            wid70px; background-color: darkslategrey; color: white;
            padding: 10px 10px; margin: 5px 0; border: none;
            border-radius: 3px; cursor: pointer; display: inline-block;
        }

        #reset {
            text-decoration: none; color: white;  font-size: 13px;
            text-align:center;
        }

        #create {
            position : relative;
            right : 1200px;
            bottom: 50px;
        }

        input[type=text],input[type=password] {
            wid 15%; /*입력 칸 (input field) 의 폭을 지정하기 위해, 폭 속성 (width property) 를 사용하였습니다.*/
            padding: 12px 20px; margin: 8px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px;
            box-sizing: border-box;
        }

        select,option {
            wid 10X%; /*입력 칸 (input field) 의 폭을 지정하기 위해, 폭 속성 (width property) 를 사용하였습니다.*/
            padding: 12px 20px; margin: 8px 0; display: inline-block; border: 1px solid #ccc; border-radius: 4px;
            box-sizing: border-box;
        }

        .pagination .hide {
            display:block;height:0;wid0;font-size:0;line-height:0;margin:0;padding:0;overflow:hidden;}

        .pagination{padding:19px;text-align:center;}

        .pagination a{display:inline-block;wid23px;height:23px;padding-top:2px;vertical-align:middle;}

        .pagination .btn_arr{text-decoration:none;}

        .pagination .btn_arr, .pagination .on{margin:0 3px;padding-top:0;border:1px solid #ddd; border-radius:30px;

            /* background:url(/front/img/com/btn_pagination.png) no-repeat; */}

        .pagination .on{padding-top:1px;height:22px;color:#fff;font-weight:bold;background:#000;}

        .pagination .on:hover{text-decoration:none;}

        .btn{
            wid 20%; background-color: darkslategrey; color: white;
            padding: 14px 20px; margin: 8px 0; border: none;
            border-radius: 4px; cursor: pointer; text-decoration: none;
        }
    </style>
</head>
<body>
<br /> <br /> <br />
<!--검색영역-->
<div id="adv-search" class="input-group" >
    <div class="input-group-btn">
        <div class="btn-group" role="group">
            <div class="dropdown dropdown-lg" >
                <div class="dropdown-menu dropdown-menu-right" role="menu">

                    <!--/* 검색 form */-->
                    <form id="searchForm" action="/opmanager/user/list" method="get"
                               class="form-horizontal" role="form">
                        <!-- /* 현재 페이지 번호, 페이지당 출력할 데이터 개수, 페이지 하단에 출력할 페이지 개수 Hidden 파라미터 */ -->
                        <input type="hidden" name="currentPageNo" value="1"/>
                        <input type="hidden" name="recordsPerPage" value="${user.recordsPerPage}"/>
                        <input type="hidden" name="pageSize" value="${user.pageSize}"/>

                        <div class="form-group">
                            <label>검색 유형</label>
                            <select name="searchType" class="form-control">
                                <option value="">전체</option>
                                <option value="name">이름
                                </option>
                                <option value="loginId">아이디
                                </option>
                                <option value="email">이메일
                                </option>
                                <option value="zipcode">우편번호
                                </option>
                                <option value="address">주소
                                </option>
                                <option value="addressDetail">상세주소
                                </option>
                                <option value="phoneNumber">전화번호
                                </option>
                            </select>

                            <input type="text" name="searchKeyword" class="form-control"/>

                            <button type="submit" class="btn btn-primary">
                                <span class="glyphicon glyphicon-search" aria-hidden="true"></span>검색
                            </button>

                            <div class="reset">
                                <td><a id="reset" href="/opmanager/user/list">검색 초기화</a> </td>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row">
        <table class="table table-hover type09" >
            <thead>
            <tr class="warning">
                <th class="col-sm-1">No</th>
                <th class="col-sm-5">이름</th>
                <th class="col-sm-5">아이디</th>
                <th class="col-sm-3">이메일</th>
                <th class="col-sm-3">가입일</th>

                <th class="col-sm-3">우편번호</th>
                <th class="col-sm-3">주소</th>
                <th class="col-sm-3">상세주소</th>
                <th class="col-sm-3">전화번호</th>
                <th class="col-sm-3">SMS수신여부</th>
                <th class="col-sm-3">직위</th>

                <th class="col-sm-3">수정</th>
                <th class="col-sm-3">삭제</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach items="${userList}" var="userList">
            <tr>
                <td>${userList.pagingId}</td>
                <td>${userList.name}</td>
                <td>${userList.loginId}</td>
                <td>${userList.email}</td>
                <td>${userList.createdDate}</td>

                <td>${userList.userDetail.zipcode}</td>
                <td>${userList.userDetail.address}</td>
                <td>${userList.userDetail.addressDetail}</td>
                <td>${userList.userDetail.phoneNumber}</td>
                <td>${userList.userDetail.getReceiveSmsTitle()}</td>
                <td>${userList.userRole.getAuthorityTitle()}</td>

                <!--수정  아이디 넘어감 성공-->
                <td>
                    <c:set var="currentPageNo" value="${user.currentPageNo}" scope="session"/>
                    <a id="edit" href="/opmanager/user/edit/${userList.id}${user.makeQueryString(currentPageNo)}">수정</a>
                </td>
                <td>
                    <form action="/opmanager/user/delete/${userList.id}${user.makeQueryString(currentPageNo)}" method="post">
                        <!--href는 get으로 처리된다. form도 안될땐 method="post"써줘야한다.-->
                        <button class="delete" type="submit" >삭제</button>
                    </form>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<!--페이징영역-->
<c:if test="${user != null and user.paginationInfo.totalRecordCount > 0}">

    <div fragment="pagination" aria-label="Page navigation" class="text-center">

        <ul class="pagination">
            <c:if test="${user.paginationInfo.hasPreviousPage == true}">
                <li onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(1)}'">
                    <a href="javascript:void(0)" class="btn_arr first" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a>
                </li>
            </c:if>
            <c:if test="${user.paginationInfo.hasPreviousPage == true}">
                <c:set var="firstPage" value="${user.paginationInfo.firstPage}" scope="session"/>
                <li onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(firstPage - 1)}'">
                    <a href="javascript:void(0)" class="btn_arr prev" aria-label="Previous"><span aria-hidden="true">&lsaquo;</span></a>
                </li>
            </c:if>

            <c:forEach begin="${user.paginationInfo.firstPage}" end="${user.paginationInfo.lastPage}" var="idx">
                <c:if test="${idx == user.currentPageNo}">
                    <li class="active">
                        <a href="javascript:void(0)" class="on"
                           onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(idx)}'">${idx}</a>
                    </li>
                </c:if>
                <c:if test="${idx != user.currentPageNo}">
                    <li class="">
                        <a href="javascript:void(0)" class="on"
                           onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(idx)}'">${idx}</a>
                    </li>
                </c:if>
            </c:forEach>

            <c:if test="${user.paginationInfo.hasNextPage == true}">
                <c:set var="nextPage" value="${user.paginationInfo.lastPage}" scope="session"/>
                <li onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(nextPage + 1)}'">
                    <a href="javascript:void(0)" class="btn_arr next"
                       aria-label="Next"><span aria-hidden="true">&rsaquo;</span></a>
                </li>
            </c:if>
            <c:if test="${user.paginationInfo.hasNextPage == true}">
                <c:set var="lastPage" value="${user.paginationInfo.totalPageCount}" scope="session"/>
                <li onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(lastPage)}'">
                    <a href="javascript:void(0)" class="btn_arr last" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
                </li>
            </c:if>
        </ul>
    </div>
</c:if>

<!--등록 성공-->
<form action="/opmanager/user/create">
    <input type="submit" value="등록" id="create" style="float:right">
</form>

</div>


<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">
	$(".delete").click (
		function() {
			if (confirm("정말 삭제하시겠습니까 ?") == true) {

			} else {
				return false;
			}
        });

</script>
</body>

</html>