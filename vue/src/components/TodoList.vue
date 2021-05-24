<template>
  <div class="container">
    <AppHeader />

    <button class="btn btn-info" @click="goAddTodo">연락처 추가</button> &nbsp;&nbsp;
    <button class="btn btn-info" @click="reload">새로 고침</button>
    <div class="panel panel-default panel-borderless">
      <div class="panel-body">
        <div class="row">
          <ul class="list-group">


            <TodoItem/>

          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Constant from './Constant';

export default {
  name:"todoList",

  //props : ['todolist'],

  mounted() {     //상태를 보고 액션수행
    if (!this.$store.state.todolist || this.$store.state.todolist.length === 0) {
      //this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$route.token });
    }
  },

  methods : {
      goAddTodo() {
        this.$store.dispatch(Constant.INITIALIZE_TODOITEM);
        this.$router.push({ name:"addTodo" });
      },
      reload() {
        //this.$store.dispatch(Constant.LOAD_TODOLIST, { token: this.$route.token });
    }
  }
}
</script>

<style>
</style>
