<template>
  <div class="container">
    <AppHeader />

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

        <div class="row">
          <ul class="list-group">
            <TodoItem v-for="todoitem in todolist" :key="todoitem.id" :todoitem="todoitem" />
          </ul>
        </div>

        <button class="btn btn-info" @click="goAddTodo">회원 등록</button> &nbsp;&nbsp;
        <button class="btn btn-info" @click="reload">새로 고침</button>

        <!--페이징 영역-->
        <paginate ref="pagebuttons"
                  :page-count="totalpage"
                  :page-range="7"
                  :margin-pages="3"
                  :click-handler="pageChanged"
                  :prev-text="'이전'"
                  :next-text="'다음'"
                  :container-class="'pagination'"
                  :page-class="'page-item'">
        </paginate>

      </div>
    </div>
  </div>
</template>

<script>
import Constant from "@/components/Constant";
import TodoItem from '@/components/TodoItem';
import Paginate from 'vuejs-paginate';    //왜 설치할까???
// import { mapState } from 'vuex';    //mapState ??

export default {
  name:"todoList",
  components : { TodoItem, Paginate },
  //props : ['todolist'],

  //검색
  data : function() {
    return { name: ''};
  },

  // computed : mapState([    // mapState ??
  //   'keywordlist'
  // ]),

  computed : {
    todolist() {
      return this.$store.state.todolist;
    },
    //페이징
    totalpage : function() {
       return Math.floor((this.todolist.totalcount - 1) / this.todolist.pagesize) + 1;
    }
  },

  mounted() {     //변이를 일으키기 위해 this.$store.state 와 같이 저장소의 상태에 접근
    if (!this.$store.state.todolist || this.$store.state.todolist.length === 0) {
      this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$route.token });
    }

    //페이징
    var page = 1;
    if (this.$route.query && this.$route.query.page) {
      page = parseInt(this.$route.query.page);
    }
   // this.$store.dispatch(Constant.FETCH_CONTACTS, { pageno:page });
    this.$refs.pagebuttons.selected = page-1;

  },

  //페이징
  watch : {
    '$route' : function(to) {
      if (to.query.page && to.query.page != this.contactlist.pageno) {
        var page = to.query.page;
        this.$store.dispatch(Constant.FETCH_CONTACTS, { pageno:page });
        this.$refs.pagebuttons.selected = page-1;
      }
    }
  },

  methods : {
    goAddTodo() {
      this.$store.dispatch(Constant.INITIALIZE_TODOITEM);
      this.$router.push({ name:"opmanagerForm" });
    },
    reload() {
      this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$route.token });
    },

    //페이징
    pageChanged : function(page) {
      this.$router.push({ name: 'contacts', query: { page: page }})
    },
    //검색
    keyupEvent : function(e) {
      var val = e.target.value;
      this.$store.dispatch(Constant.SEARCH_CONTACT, { name: val });
      this.name = "";
    }
  }
}
</script>

<style>
</style>
