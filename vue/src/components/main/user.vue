<template>
  <div class="hello">
    <div v-if="token">   <!--로그인 후에는 화면이 다르게뜨도록한다.-->
      <br><br>
      <router-link to="/user/password-check" >정보수정</router-link> <br><br>
      <a href="/" @click="logout" class="logout">로그아웃</a>
    </div>
    <div v-else>
      <router-link to="/user/login" >로그인</router-link> <br><br>
      <router-link to="/user/create">회원가입</router-link>
    </div>
    {{ token }}

  </div>
</template>

<script>
//import Constant from "@/components/Constant";
//import {getUserInfoFromToken} from "@/tokenutil";

export default {
  computed : {
    token() {
      const token = localStorage.getItem("token");
      return token;
     // return this.$store.state.token;
    },
    isToken : function() {
      return this.$store.state.token;
    }
  },

  methods : {
    logout() {
      localStorage.setItem("token", "");
      // return token;
      //this.$store.dispatch(Constant.SET_USER_INFO, { userInfo:null, token:"" })
      this.$router.push({ name:"userMain" })
    }
  },

  //
  // beforeCreated() {
  //   // this.$store.dispatch(Constant.SET_USER_INFO, { userInfo:this.$store.state.userInfo, token:this.$store.state.token })
  //   //const token = localStorage.getItem('token') // 로컬스토리지에서 user 데이터를 가져온다.
  //
  //   // if (token) {    //토큰이있으면  -  정보수정후 메인페이지로 보내면 토큰없는상태로 뜬다.
  //   //   const userInfo = getUserInfoFromToken();
  //   //   this.$store.commit(Constant.SET_USER_INFO, {token, userInfo} ) // Vuex suer state에 저장하기 위해 mutation을 사용한다.
  //   // }
  // }
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
