<template>
  <div class="hello">
    <div v-if="token">   <!--로그인 후에는 화면이 다르게뜨도록한다.-->
      로그인후 토큰있는지 확인 <br><br>
      <router-link to="/user/edit" >정보수정</router-link> <br><br>
      <router-link to="/user/logout" >로그아웃</router-link> <br><br>
    </div>
    <div v-else-if="beforeLogin">
      <router-link to="/user/login" >로그인</router-link> <br><br>
      <router-link to="/user/create">회원가입</router-link>
    </div>
    {{ isToken }}
  </div>
</template>

<script>
export default {
  computed : {
    beforeLogin() {
      return 1;
    },
    token() {
      return this.$store.state.token;
    },
    isToken : function() {
      return this.$store.state.token;
    }
  },
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
