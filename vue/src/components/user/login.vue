<template>
  <form v-on:submit.prevent="submitForm" id="target">
    <div>
      <label for="loginId">id:</label>
      <input id="loginId" type="text" v-model="loginId" />
    </div>
    <div>
      <label for="password">PW:</label>
      <input id="password" type="password" v-model="password"  />
    </div>
    <button type="submit">login</button>
    <a href="" v-if="token" @click.prevent="onClickLogout">로그인후 토큰있는지 확인</a>
    {{ isToken }}
  </form>
</template>

<script>
import Constant from "@/components/Constant";
import axios from "axios";
import {getUserInfoFromToken} from "@/tokenutil";

export default {
  user:function(){
    return{
      loginId : '',
      password : ''
    }
  },
  computed : {
    token() {
      return this.$store.state.token;
    },
    isToken : function() {
      return this.$store.state.token;
    }
  },
  methods:{
    submitForm:function(){
      console.log(this.loginId, this.password);

      var user = {
        loginId: this.loginId,
        password: this.password,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json; charset = utf-8'
        }
      }
      let form = new FormData();
      form.append('loginId', user.loginId);
      form.append('password', user.password);

      axios.post(`http://localhost:8080/api/user/login`, form)    //form에 user안의 정보 포함시켜 보냄 -> user로 받음
          .then((response)=> {
            let token = response.data
            window.localStorage.setItem("token", token);    //컨트롤러에서 200뜸

            const userInfo = getUserInfoFromToken();
            this.$store.commit(Constant.SET_USER_INFO, { token, userInfo });
            this.$router.push({ name:"userMain" });

          })
          .catch(()=>{

          })
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
