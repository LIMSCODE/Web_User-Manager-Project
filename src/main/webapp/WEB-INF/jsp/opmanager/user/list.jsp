<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<title>관리자 메인페이지</title>
<body>
<br /> <br /> <br />
<!--검색영역-->
<sec:authorize access="hasAnyRole('ROLE_OPMANAGER')">
    <div id="adv-search" class="input-group" >
        <div class="input-group-btn">
            <div class="btn-group" role="group">
                <div class="dropdown dropdown-lg" >
                    <div class="dropdown-menu dropdown-menu-right" role="menu">

                        <!--/* 검색 form */-->
                        <form id="searchForm" action="/api/opmanager/user/ajax-list" method="get"
                              class="form-horizontal" role="form">
                            <!-- /* 현재 페이지 번호, 페이지당 출력할 데이터 개수, 페이지 하단에 출력할 페이지 개수 Hidden 파라미터 */ -->
                            <input type="hidden" name="currentPageNo" value="1"/>
                            <input type="hidden" name="size" value="${user.size}"/>
                            <input type="hidden" name="page" value="${user.page}"/>

                            <div class="form-group">
                                <label>검색 유형</label>
                                <select name="searchType" id="searchType" class="form-control">
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

                                <input type="text" name="searchKeyword" id="searchKeyword" class="form-control"/>

                                <button type="submit" class="btn btn-primary" >
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
        <br><br><br>
        <form action="/opmanager/user/create">
            <input type="submit" value="등록" id="create" style="float:right">
        </form>
    </div>
    </div>
</sec:authorize>

<script type="text/javascript">

	//list컨트롤러로 화면 호출후, json데이터 변환해서 화면에 뿌리기 컨트롤러
	$(document).ready(function() {

		$.ajax({
			dataType : "json",
			url : "/api/opmanager/user/ajax-list",

			success : function(data){
				resultHtml(data);
			},
			error : function(){ alert("로딩실패!");
			}
		});
	});

	//검색시 이벤트
	$("#searchForm").on("submit", function(e) {

		e.preventDefault();

		let searchForm = new FormData($("#searchForm")[0]);
		let searchType = $("#searchType").val();
        let searchKeyword = $("#searchKeyword").val();

		$.ajax({
			url : "/api/opmanager/user/ajax-list?searchType=" + searchType + "&searchKeyword=" + searchKeyword,
			type : "get",
			data : searchForm,
			datatype: 'json',
			processData: false,
			contentType: false,

			success : function(data) {
				resultHtml(data);
			},
			error : function() {
				alert("검색 에러");
			}
		});
	});

	//페이지 넘버 onclick시
	function movePage(pageNum) {

		$.ajax({
			dataType : "json",
			url : "/api/opmanager/user/ajax-list?page=" + pageNum,

			success : function(data){
				resultHtml(data);
			},
			error : function(){ alert("로딩실패!");
			}
		});
	}


	//테이블 완성
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

		$.each(data, function(key, value) {     //data - 컨트롤러에서 userPage값 받음

			if (key == "content") {
				for (var i = 0; i < value.length; i++) {
					html += "<tr>";
					html += "<td>" + value[i].pagingId + "</td>";
					html += "<td>" + value[i].name + "</td>";
					html += "<td>" + value[i].loginId + "</td>";
					html += "<td>" + value[i].email + "</td>";
					html += "<td>" + value[i].createdDate + "</td>";
					html += "<td>" + value[i].userDetail.zipcode + "</td>";
					html += "<td>" + value[i].userDetail.address + "</td>";
					html += "<td>" + value[i].userDetail.addressDetail + "</td>";
					html += "<td>" + value[i].userDetail.phoneNumber + "</td>";
					html += "<td>" + value[i].userDetail.receiveSmsTitle + "</td>";
					html += "<td>" + value[i].userRole.authorityTitle + "</td>";
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


        //페이징
        for (var num = 0; num < data.totalPages; num++) {

			if (num == data.pageable.pageNumber) {
				html += "<li class='active'>";
				html += "<a class='on' onclick='movePage(" + num + ");'>"  //뷰에서 페이지 넘길때의 값을 컨트롤러로 전송
				html += num + 1;
				html += "</a>";
				html += "</li>";
				//html += "<a href='/opmanager/user/list?page=" + num +"'"+ "class='on'  >"    //onclick(페이지넘버 매개변수)

			}else {
				html += "<li class=''>";
				html += "<a class='on' onclick='movePage(" + num + ");'>"
				html += num + 1;
				html += "</a>";
				html += "</li>";
			}
		}

		$("#display").empty();
		$("#display").append(html);
	}

	function deleteUser(long) {

		if (confirm("정말 삭제하시겠습니까 ?") == true) {

			$.ajax({
				url : "/api/opmanager/user/delete/" + long,
				type : "post",

				success : function(data) {
					location.reload();
				},
				error : function() {
					alert("실패");
				}
			});

		} else {
			return false;
		}
	};

</script>
</body>
