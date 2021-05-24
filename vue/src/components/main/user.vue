<template>
  <div class="hello">
    <div v-if="token">   <!--로그인 후에는 화면이 다르게뜨도록한다.-->
      <br><br>
      <router-link to="/user/password-check" >정보수정</router-link> <br><br>
      <a href="/" @click="logout" class="logout">로그아웃</a>
    </div>
    <div v-else-if="beforeLogin">
      <router-link to="/user/login" >로그인</router-link> <br><br>
      <router-link to="/user/create">회원가입</router-link>
    </div>
    {{ isToken }}
  </div>
</template>

<script>
import Constant from "@/components/Constant";

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
  methods : {
    logout() {
      this.$store.dispatch(Constant.SET_USER_INFO, { userInfo:null, token:"" })
      this.$router.push({name:"userMain"})
    }
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
