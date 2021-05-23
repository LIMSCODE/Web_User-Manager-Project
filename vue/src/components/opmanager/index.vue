<template>
  <div class="hello">
    <h1>관리자 메인페이지</h1>
    <router-link to="/opmanager/user/list">회원 목록</router-link>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
