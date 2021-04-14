<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<title>관리자 메인페이지</title>
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
                                <option value="all">전체</option>
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
            <c:forEach items="${userPage.content}" var="userList">
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

<ul class="pagination">
    <c:if test="${!userPage.first}">
        <li class="previous">
            <a href="javascript:void(0)" class="on"
               onclick="location.href='/opmanager/user/list' + '?page=' + ${userPage.number - 1}">&larr;</a>
        </li>
    </c:if>

    <c:forEach begin="${firstPage}" end="${lastPage}" var="idx">
        <c:if test="${idx == userPage.pageable.pageNumber + 1}">
            <li class="active">
                <a href="javascript:void(0)" class="on"
                   onclick="location.href='/opmanager/user/list' + '?page=' + ${idx-1}">${idx}</a>
            </li>
        </c:if>
        <c:if test="${idx != userPage.number + 1}">
            <li class="">
                <a href="javascript:void(0)" class="on"
                   onclick="location.href='/opmanager/user/list' + '?page=' + ${idx-1}">${idx}</a>
            </li>
        </c:if>
    </c:forEach>

    <c:if test="${!userPage.last}">
        <li class="previous">
            <a href="javascript:void(0)" class="on"
               onclick="location.href='/opmanager/user/list' + '?page=' + ${userPage.number + 1}">&rarr;</a>
        </li>
    </c:if>
    </p>
</ul>

<!--등록 성공-->
<form action="/opmanager/user/create">
    <input type="submit" value="등록" id="create" style="float:right">
</form>

</div>


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
