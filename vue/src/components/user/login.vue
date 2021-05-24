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
// import axios from 'axios';
// import {getUserInfoFromToken} from "@/tokenutil";
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
     // var url = 'http://localhost:8080/api/user/login';
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

      // this.$store.dispatch(Constant.LOGIN, {form})
      axios.post(`http://localhost:8080/api/user/login`, form)
          .then((response)=> {
            // if (response.data.status === "success") {
            let token = response.data
            window.localStorage.setItem("token", token);    //컨트롤러에서 200뜸

            const userInfo = getUserInfoFromToken();
            this.$store.commit(Constant.SET_USER_INFO, { token, userInfo });
            this.$router.push({ name:"userMain" });
            //commit : 변이를 수행한다. 페이로드값을 매개변수로. state에 저장한다.
            // payload.callback(token);
            // } else {
            //     payload.callback(respoㅇnse.data);
            // }
          })
          .catch(()=>{
            //payload.callback({ status:"fail", message:"로그인 실패222 : " + error});
          })

    //   axios.post(url, form)
    //       .then(function(response){
    //         console.log(response);
    //         localStorage.setItem('token', response.data); //로컬스토리지에 저장후
    //
    //         const userInfo = getUserInfoFromToken();
    //
    //         //액션을 수행한다.
    //         this.$store.dispatch(Constant.SET_USER_INFO, { response, userInfo })
    //
    //         window.location.href = "/";
    //       })
    //       .catch(function(response){
    //         console.log(response);
    //       });
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
