<template>
  <div class="container">
    <AppHeader />
    <div >
    <div class="panel panel-default panel-borderless">
      <div class="panel-body">
        <!--검색 영역-->
        <div>
          <p>
            이름 : <input type="text" v-model.trim="name" placeholder="두글자 이상 입력후 엔터!"
                        @keyup.enter="keyupEvent" /><br />
          </p>
        </div>

        <!--/* 원래 검색 form */-->
        <form id="searchForm" action="/api/opmanager/user/ajax-list" method="get" class="form-horizontal" role="form">
          <!-- /* 현재 페이지 번호, 페이지당 출력할 데이터 개수, 페이지 하단에 출력할 페이지 개수 Hidden 파라미터 */ -->
          <input type="hidden" name="currentPageNo" value="1"/>
          <input type="hidden" name="size" value="${user.size}"/>
          <input type="hidden" name="page" value="${user.page}"/>

          <div class="form-group">
            <label>검색 유형</label>
            <select name="searchType" id="searchType" class="form-control">
              <option value="all">전체</option>
              <option value="name">이름</option>
              <option value="loginId">아이디</option>
              <option value="email">이메일</option>
              <option value="zipcode">우편번호</option>
              <option value="address">주소</option>
              <option value="addressDetail">상세주소</option>
              <option value="phoneNumber">전화번호</option>
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

        <div class="row">
          <table class='' border='2px' >
            <thead>
            <tr>
              <th>Id</th>
              <th>아이디</th>
              <th>이름</th>
              <th>이메일</th>
              <th>우편번호</th>
              <th>주소</th>
              <th>상세주소</th>
              <th>전화번호</th>
              <th>이메일 수신여부</th>
              <th>권한</th>
              <th>수정</th>
              <th>삭제</th>
            </tr>
            </thead>
            <tbody v-for="users in userList" :key="users.id" :users="users">
            <tr :key="users.pagingId" :users="users" >
              <td>{{users.pagingId}}</td>
              <td>{{users.loginId}}</td>
              <td>{{users.name}}</td>
              <td>{{users.email}}</td>
              <td>{{users.userDetail.zipcode}}</td>
              <td>{{users.userDetail.address}}</td>
              <td>{{users.userDetail.addressDetail}}</td>
              <td>{{users.userDetail.phoneNumber}}</td>
              <td>{{users.userDetail.receiveSmsTitle}}</td>
              <td>{{users.userRole.authorityTitle}}</td>
              <td><span class="pull-right badge pointer" @click.stop="editTodo(users.id)">수정</span></td>
              <td><span class="pull-right badge pointer" @click.stop="deleteTodo(users.id)">삭제</span></td>
            </tr>
            </tbody>
          </table>
        </div>
        <button class="btn btn-info" @click="goAddTodo">회원 등록</button> &nbsp;&nbsp;
        <button class="btn btn-info" @click="reload">새로 고침</button>
      </div>
      {{ userList }}
    </div>
  </div>
  </div>
</template>

<script>
import Constant from "@/components/Constant";

export default {
  name:"list",

  mounted() {
    this.$nextTick(function () {
      this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$store.state.token });
    });

  },

  computed : {
    userList : function() {
        return this.$store.state.todolist.data.content;
    },
    token() {
      const token = localStorage.getItem("token");
      return token;
    },
    isToken : function() {
      return this.$store.state.token;
    }
  },

  methods : {
    goAddTodo() {
      this.$store.dispatch(Constant.INITIALIZE_TODOITEM);
      this.$router.push({ name:"managerCreateForm" });
    },

    reload() {
      this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$route.token });
    },

    //검색
    keyupEvent : function(e) {
      var val = e.target.value;
      this.$store.dispatch(Constant.SEARCH_CONTACT, { name: val });
      this.name = "";
    },

    //리스트
    editTodo(id) {
      this.$store.dispatch(Constant.INITIALIZE_TODOITEM, { todoitem: { ...this.todoitem } });
      this.$router.push({ name: 'managerEditForm', params: { "id" : id } })     //id값이 전달된다.
    },

    deleteTodo(id) {
      if (confirm("삭제하시겠습니까?") == true) {
        this.$store.dispatch(Constant.DELETE_TODO, {id});   //id값을 페이로드로 넘김 액션으로
      }
    }
  }
}
</script>

<style>
</style>
