
<template>
  <li :class="checked(todoitem.done)" :title="'설명 : ' + todoitem.desc"
      @click="toggleDone(todoitem.id)">

    <table class='' border='2px' >
      <thead >
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

      <tbody>
      <tr v-for="todoitem in todolist" :key="todoitem.pagingId" :todoitem="todoitem" >          <!--v-for='(emp,index) in employee' :key='index' :employee='employee'-->
        <td>{{todoitem.pagingId}}</td>
        <td>{{todoitem.loginId}}</td>
        <td>{{todoitem.name}}</td>
        <td>{{todoitem.email}}</td>

        <td>{{todoitem.userDetail.zipcode}}</td>
        <td>{{todoitem.userDetail.address}}</td>
        <td>{{todoitem.userDetail.addressDetail}}</td>
        <td>{{todoitem.userDetail.phoneNumber}}</td>
        <td>{{todoitem.userDetail.receiveSmsTitle}}</td>
        <td>{{todoitem.userRole.authorityTitle}}</td>
        <td><span class="pull-right badge pointer" @click.stop="editTodo(todoitem.id)">수정</span></td>
        <td><span class="pull-right badge pointer" @click.stop="deleteTodo(todoitem.id)">삭제</span></td>
      </tr>
      </tbody>
    </table>
  </li>
</template>

<script>
import Constant from './Constant';
export default {
  name : "todoItem",
  props : ['todoitem'],
  computed : {
    todolist: function() {
      return this.$store.state.todolist;
    }
  },
  methods : {
    editTodo(id) {
      this.$store.dispatch(Constant.INITIALIZE_TODOITEM, { todoitem: { ...this.todoitem } });
      this.$router.push({ name: 'opmanagerForm2', params: { "id" : id } })     //id값이 전달된다.
    },
    deleteTodo(id) {
      if (confirm("정말로 삭제하시겠습니까?") == true) {
        this.$store.dispatch(Constant.DELETE_TODO, {id});   //id값을 페이로드로 넘김 액션으로
      }
    },
    checked(done) {
      return { "list-group-item":true, "list-group-item-success":done };
    },
    toggleDone(id) {
      this.$store.dispatch(Constant.TOGGLE_DONE, { id });
    }
  }
}
</script>

<style>
</style>
