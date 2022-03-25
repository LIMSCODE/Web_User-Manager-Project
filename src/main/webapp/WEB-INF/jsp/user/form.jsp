<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>
<%response.setContentType("text/html; charset=UTF-8");%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8"/>
    <title>회원등록</title>
    <link rel="stylesheet"  type="text/css" href="${pageContext.request.contextPath}/webapp/content/common.css">
    <style>
        span>p {
            color: #bd2130;
        }
    </style>
</head>
<body>
<%--@elvariable id="user" type="com.onlinepowers.springmybatis.user.User"--%>
<form:form modelAttribute="user" method="post" id="target" >
    <c:if test="${id != null}">
    <span>
        <form:hidden path="id" value="${id}"/>
    </span>
    </c:if>

    <c:if test="${id != null}">
    <span>
        *이름<form:input type="text" path="name" maxlength="12" readonly="true"/> <br>
        <p><form:errors path="name"/></p>
        *아이디<form:input path="loginId" maxlength="12" readonly="true"/>
        <p><form:errors path="loginId"/></p>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
         *이름<form:input path="name" maxlength="12"/> <br>
        <p><form:errors path="name"/></p>
         *아이디<form:input path="loginId" onkeyup="resetIdCheckStatus();"  maxlength="12"/>
        <form:errors path="loginId"/>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
    <button type ="button" class="checkId" id="idCheck"> 아이디중복확인 </button>
        <input type="hidden" name="idCheckStatus" id="idCheckStatus" value="0" >
    <span class="msg" id="msg"> </span>
    </span>
    </c:if>
    <br>

    <c:if test="${id != null}">
    <span>
        *비밀번호 <form:password path="password" maxlength="8"/> <br>
        <p><form:errors path="password"/></p>
        *비밀번호 확인 <input type="password" name="passwordConfirm"  id="passwordConfirm" maxlength="8"> <br>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
         *비밀번호<form:password path="password" id="createPassword" maxlength="8"/> <br>
        <p><form:errors path="password"/></p>
         *비밀번호 확인<input type="password" name="passwordConfirm"  id="createPasswordConfirm" maxlength="8"> <br>
    </span>
    </c:if>
    <span id="isSame" class="same"></span>

    <span>
         *이메일<form:input path="email" maxlength="30"/> <br>
        <p><form:errors path="email"/></p>
    </span>

    <c:if test="${id != null}">
    <span>
        *우편번호 <form:input path="userDetail.zipcode" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.zipcode"/></p>
        *주소 <form:input path="userDetail.address" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.address"/></p>
        *상세주소 <form:input path="userDetail.addressDetail" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.addressDetail"/></p>
        *전화번호 <form:input path="userDetail.phoneNumber"  maxlength="30" /> <br>
         <p> <form:errors path="userDetail.phoneNumber"/></p>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
         *우편번호<form:input path="userDetail.zipcode" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.zipcode"/></p>
         *주소<form:input path="userDetail.address" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.address"/></p>
         *상세주소<form:input path="userDetail.addressDetail" maxlength="30" /> <br>
        <p> <form:errors path="userDetail.addressDetail"/></p>
         *전화번호<form:input path="userDetail.phoneNumber"  maxlength="30" /> <br>
        <p> <form:errors path="userDetail.phoneNumber"/></p>
    </span>
    </c:if>

    *SNS수신여부
    <c:if test="${id != null}">
    <span>
        <form:radiobutton path="userDetail.receiveSms" value="1" label="수신" />
        <form:radiobutton path="userDetail.receiveSms" value="0" label="수신x" />
        <input type="hidden" id="receiveSms" value="${userDetail.receiveSms}">   <!--DB에서 가져온 값-->
        <p><form:errors path="userDetail.receiveSms"/></p>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
        <form:radiobutton path="userDetail.receiveSms" value="1" label="수신" />
        <form:radiobutton path="userDetail.receiveSms" value="0" label="수신x" />
        <p><form:errors path="userDetail.receiveSms"/></p>
    </span>
    </c:if>

    <span>
        <input type="hidden" name="userRole.authority" value="ROLE_USER" >
    </span>

    <c:if test="${id != null}">
    <span>
        <input type="submit" class="submit" value="수정"  formaction="/api/user/edit/${id}" ><br><br>
        <td><a class="btn" href="/">메인화면</a> </td>
    </span>
    </c:if>

    <c:if test="${id == null}">
    <span>
        <input type="submit" class="submit" value="등록" formaction="/api/user/create">
    </span>
    </c:if>
</form:form>

<script type="text/javascript" src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script type="text/javascript">

	let $id = $("#id");
	let $name= $("#name");
	let $loginId= $("#loginId");
	let $password = $("#password");
	let $passwordConfirm = $("#passwordConfirm");
	let $createPassword = $("#createPassword");
	let $createPasswordConfirm = $("#createPasswordConfirm");
	let $email = $("#email");

	let $zipcode = $("#userDetail.zipcode");
	let $address = $("#userDetail.address");
	let $addressDetail = $("#userDetail.addressDetail");
	let $phoneNumber = $("#userDetail.phoneNumber");

	let $idCheckStatus = $("#idCheckStatus");     //아이디 중복체크 했는지 여부

	$(document).ready(function() {

		if ($id.val() != null) {        //수정하는 창에 set
			$.ajax({
				dataType : "json",
				url : "/api/user/edit-detail",

				headers: {      //헤더에 토큰 없으면 filter에서 401에러로 보낸다.
					'Authorization': 'Bearer ' + localStorage.getItem('wtw-token')
				},
				success : function(data){
					editFormDetail(data);
				},
				error : function(){ alert("로딩실패!");
				}
			});
		}
	});

	//수정 폼데이터 채우기
	function editFormDetail(data) {
		//로그인객체의 각각의 값을 가져옴 -> id값으로 불러온 태그의 value
		$name.val(data.name);
		$loginId.val(data.loginId);
		$email.val(data.email);
		$zipcode.val(data.userDetail.zipcode);
		$address.val(data.userDetail.address);
		$addressDetail.val(data.userDetail.addressDetail);
		$phoneNumber.val(data.userDetail.phoneNumber);
		$receiveSms.val(data.userDetail.receiveSms);
	}


	//최종 제출시 이벤트
	$("#target").on("submit", function(e) {

        e.preventDefault();

		if ($name.val() == "") {
			alert("이름을 입력해주세요");
			$name.focus();
			return false;
		}

		if ($loginId.val() == "") {
			alert("아이디 입력해주세요");
			$loginId.focus();
			return false;
		}

		let idReg = /^[a-z]+[a-z0-9]{5,19}/g;

		if(idReg.test($loginId.val()) == false) {
			alert("아이디는 영문자로 시작하는 6~20자 영문자 또는 숫자이어야 합니다.");
			$loginId.focus();
			return false;
		}

		if ($createPassword.val() == "") {
			alert("비밀번호 입력해주세요");
			$createPassword.focus();
			return false;
		}

		if ($createPasswordConfirm.val() == "") {
			alert("비밀번호 확인 입력해주세요");
			$createPasswordConfirm.focus();
			return false;
		}

		if ($createPassword.val() != $createPasswordConfirm.val()) {
			alert("비밀번호 일치하지 않음");
			$createPassword.focus();
			return false;
		}

		if ($password.val() != $passwordConfirm.val()) {
			alert("비밀번호 일치하지 않음");
			$password.focus();
			return false;
		}

		if ($email.val() == "") {
			alert("이메일 입력해주세요");
			$email.focus();
			return false;
		}

		//이메일 정규식
		let exptext = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

		//이메일 형식이 알파벳+숫자@알파벳+숫자.알파벳+숫자 형식이 아닐 경우
		if (exptext.test($email.val()) == false) {
			alert("이메일 형식이 올바르지 않습니다.");
			return false;
		}

		if ($("#userDetail.zipcode").val() == "") {
			alert("우편번호 입력해주세요");
			$zipcode.focus();
			return false;
		}

		if ($address.val() == "") {
			alert("주소 입력해주세요");
			$address.focus();
			return false;
		}

		if ($addressDetail.val() == "") {
			alert("상세주소 입력해주세요");
			$addressDetail.focus();
			return false;
		}

		if ($phoneNumber.val() == "") {
			alert("전화번호 입력해주세요");
			$phoneNumber.focus();
			return false;
		}

		if ($idCheckStatus.val() == 0) {
			alert("아이디 중복확인 체크해주세요.");
			return false;
		}

		if($("input:radio[name='userDetail.receiveSms']").is(":checked") == false) {
			alert("sms 수신여부 선택해주세요");
			return false;
		}

        if ($id.val() == null) {

            let createFormData = new FormData($("#target")[0]);

            $.ajax({
                url : "/api/user/create",
                type : "post",
                data : createFormData,
                datatype: 'json',   //생성된 토큰을 받는다.
                processData: false,
                contentType: false,

	            headers: {      //헤더에 토큰 없으면 filter에서 401에러로 보낸다.
		            'Authorization': 'Bearer ' + localStorage.getItem('wtw-token')
	            },
                success : function(token) {
                    var expireDay = 24 * 60 * 60 * 1000; //1일
                    //document.cookie = "X-AUTH-TOKEN=" + token + expireDay +"; path=/";  //쿠키에 토큰 저장
                    //window.location.href="/";
                },
                error : function() {
                    alert("실패");
                }
            });
        }

        if ($id.val() != null) {

            var editFormData = new FormData($("#target")[0]);

            $.ajax({
                url : "/api/user/edit/" + $id.val(),
                type : "put",
                data : editFormData,
                datatype: 'json',
                processData: false,
                contentType: false,

	            headers: {      //헤더에 토큰 없으면 filter에서 401에러로 보낸다.
		            'Authorization': 'Bearer ' + localStorage.getItem('wtw-token')
	            },
                success : function(data) {
                    alert("정보수정 성공");
                    window.location.href="/";
                },
                error : function() {
                    alert("실패");
                }
            });
        }
	});

	//아이디 중복 체크시 이벤트
	$("#idCheck").click (function() {

		let $loginId = $("#loginId");
		let $loginMessage = $("#msg");
		let $idCheckStatus = $("#idCheckStatus");     //아이디 중복체크 누르면 1로 변경

		if ($loginId.val() == "") {
			alert("아이디를 입력해주세요");
			return false;
		}

		if ($loginMessage.val() == "사용 불가") {
			return false;
		}

		if ($idCheckStatus.val() == 0) {
			$idCheckStatus.val(1) ;
		}

		let query = { "loginId" : $loginId.val() };

		$.ajax({
			url : "/api/user/check-id",
			type : "post",
			data : query,
			datatype: "json",
			success : function(data) {

				if (data.isDuplicated == false) {
					$("#msg").text("사용 불가");
					$("#msg").attr("style", "color:#f00");
					alert("해당 아이디 존재");
					$(".submit").attr("disabled", "disabled");

				} else {
					$("#msg").text("사용 가능");
					$("#msg").attr("style", "color:#00f");
					$(".submit").removeAttr("disabled");
				}
			},
			error : function() {
				alert("실패");
			}
		});
	});

	function resetIdCheckStatus() {      //아이디값 다시입력시 0으로 바뀌도록 onkeyup함수 , 사용가능 텍스트도 안뜨도록

		let $idCheckStatus = $("#idCheckStatus");
		$idCheckStatus.val(0);
		$("#msg").text("");
		$("#msg").removeAttr("style");
		$(".submit").removeAttr("disabled");
	}

	$(document).ready(function() {

		let $receiveSmsTag =  $("input:radio[name=receiveSms]");

		$receiveSmsTag.each(function() {

			let $receiveSms = $("#receiveSms");     //db에 저장된 값 가져옴 (수정시 값존재, 회원가입시 값존재 x)

			$(this).prop("checked", $(this).val() == $receiveSms.val());    //db에 저장된값과 같은태그이면, checked속성부여

		});
	});

</script>

</body>

</html>
