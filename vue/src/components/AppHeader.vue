<template>
  <div id="nav">
    <div class="row">
      <div class="col-xs-12 col-sm-12 col-md-12 text-right">
        <router-link to="/">Home</router-link> | <router-link to="/opmanager/user/login">Login</router-link> |
      </div>
      <div class="col-xs-12 col-sm-12 col-md-12 text-right">
        {{ userInfo ? userInfo.sub + " 님 안녕하세요" : "로그인하지 않았음" }}&nbsp;&nbsp;
        <a href="#" v-if="userInfo" @click="logout" class="logout">로그아웃</a>
      </div>
    </div>
    <br />
    <br />
  </div>
</template>

<script>
import Constant from './Constant';
export default {
  name : "AppHeader",
  computed : {
    userInfo() {
      return this.$store.state.userInfo;
    }
  },
  methods: {
    logout() {
      this.$store.dispatch(Constant.SET_USER_INFO, { userInfo:null, token:"" })
      this.$router.push({name:"userMain"})
    }
  },
}
</script>

<style scoped>
a {
  color: inherit;
  text-decoration: underline;
}
a:hover
{
  color:#00A0C6;
  text-decoration:underline;
  cursor:pointer;
}
</style>
