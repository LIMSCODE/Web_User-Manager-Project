<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<title>관리자 메인페이지</title>
<body>
<br /> <br /> <br />
<!--검색영역-->
<div sec:authorize="isAuthenticated()">
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
                        <input type="hidden" name="size" value="${user.size}"/>
                        <input type="hidden" name="page" value="${user.page}"/>

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
        <div id="display">
    </div>
</div>

<ul class="pagination">
    <c:if test="${!userPage.first}">
        <c:set var="page" value="${userPage.pageable.pageNumber}" scope="session"/>
        <li class="previous">
            <a href="javascript:void(0)" class="on"
               onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(page - 1)}'">&larr;</a>
        </li>
    </c:if>

    <c:forEach begin="${1}" end="${userPage.totalPages}" var="idx">
        <c:if test="${idx == userPage.pageable.pageNumber + 1}">
            <li class="active">
                <a href="javascript:void(0)" class="on"
                   onclick="location.href='/opmanager/user/list'  + '${user.makeQueryString(idx - 1)}'">${idx}</a>
            </li>
        </c:if>
        <c:if test="${idx != userPage.number + 1}">
            <li class="">
                <a href="javascript:void(0)" class="on"
                   onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(idx - 1)}'">${idx}</a>
            </li>
        </c:if>
    </c:forEach>

    <c:if test="${!userPage.last}">
        <c:set var="page" value="${userPage.pageable.pageNumber}" scope="session"/>
        <li class="previous">
            <a href="javascript:void(0)" class="on"
               onclick="location.href='/opmanager/user/list' + '${user.makeQueryString(page + 1)}'">&rarr;</a>
        </li>
    </c:if>
    </p>
</ul>

<form action="/opmanager/user/create">
    <input type="submit" value="등록" id="create" style="float:right">
</form>

</div>
</div>

<script type="text/javascript">

    //화면로딩시 ajax로 json데이터 변환해서 화면에 뿌리기
    $(document).ready(function() {

	    $.ajax({
		    dataType : "json",
		    url : "/opmanager/user/list1",
		    success : function(data){
		    	alert("데이터로딩 시작");
		    	resultHtml(data);
		    	},
		    error : function(){ alert("로딩실패!"); }
	    });
    });

    function resultHtml(data) {

        var html = "";
	    html += "<table class='table table-hover type09'>";
	    html += "<thead>";
	    html += "<tr class='warning'>";
	    html += "<th class='col-sm-1'>No</th>";
	    html += "<th class='col-sm-5'>이름</th>";
	    html += "<th class='col-sm-5'>아이디</th>";
	    html += "<th class='col-sm-3'>이메일</th>";
	    html += "<th class='col-sm-3'>가입일</th>";
	    html += "<th class='col-sm-3'>우편번호</th>";
	    html += "<th class='col-sm-3'>주소</th>";
	    html += "<th class='col-sm-3'>상세주소</th>";
	    html += "<th class='col-sm-3'>전화번호</th>";
	    html += "<th class='col-sm-3'>SMS수신여부</th>";
	    html += "<th class='col-sm-3'>직위</th>";
	    html += "<th class='col-sm-3'>수정</th>";
	    html += "<th class='col-sm-3'>삭제</th>";
	    html += "</tr>";
	    html += "</thead>";
	    html += "<tbody>";

	    $.each(data, function(key, value){

		    if (key == "content") {

			    for (var i = 0; i < value.length; i++) {

                    html == "<tr>";
				    html += "<td>" + value[i].pagingId + "</td>";
				    html += "<td>" +value[i].name + "</td>";
				    html += "<td>" +value[i].loginId + "</td>";
				    html += "<td>" +value[i].email + "</td>";
				    html += "<td>" +value[i].createdDate + "</td>";

				    html += "<td>" +value[i].userDetail.zipcode + "</td>";
				    html += "<td>" +value[i].userDetail.address + "</td>";
				    html += "<td>" +value[i].userDetail.addressDetail + "</td>";
				    html += "<td>" +value[i].userDetail.phoneNumber + "</td>";
				    html += "<td>" +value[i].userDetail.getReceiveSmsTitle + "</td>";
				    html += "<td>" +value[i].userRole.getAuthorityTitle + "</td>";
                    html += "<td>";
                    html += "<a id='edit' href='/opmanager/user/edit/" + value[i].id + "'>수정</a>";
                    html += "</td>";
                    html += "<td>";
                    html += "<button class='delete' type='submit'  onclick='deleteUser(" + value[i].id + ");'>삭제</button>";
				    html += "</td>";

				    html += "</tr>";
			    }
		    }
	    });

	    html += "</tbody>";
	    html += "</table>";

	    $("#display").empty();
	    $("#display").append(html);
    }

	function deleteUser(long) {

		if (confirm("정말 삭제하시겠습니까 ?") == true) {

			$.ajax({
				url : "/opmanager/user/delete/" + long,
				type : "post",
				//contentType: "application/json",
				//datatype: "string",
				success : function(data) {
					location.reload();
				},
				error : function() {
					alert(this.url);
					alert("실패");
				}
			});

		} else {
			return false;
		}
	};

</script>
</body>
