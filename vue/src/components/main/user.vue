<template>
  <div class="hello">
    <router-link to="/user/login">로그인</router-link> <br><br>
    <router-link to="/user/edit">회원가입</router-link>
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
