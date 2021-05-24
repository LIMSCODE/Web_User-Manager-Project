<template>
  <form v-on:submit.prevent="submitForm" id="target">
    <div>
      <label for="password">PW:</label>
      <input id="password" type="password" v-model="password"  />
    </div>
    <button type="submit">수정으로 이동</button>
    {{ isToken }}
  </form>
</template>

<script>
// import axios from 'axios';
// import {getUserInfoFromToken} from "@/tokenutil";
//import Constant from "@/components/Constant";
import axios from "axios";
//import {getUserInfoFromToken} from "@/tokenutil";
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
      console.log(this.password);
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
      form.append('password', user.password);
      console.log("11111111111" + localStorage.getItem('token'));   //토큰 가져와짐.

      // this.$store.dispatch(Constant.LOGIN, {form})
      axios.post(`http://localhost:8080/api/user/password-check`, form,
          {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})  // 헤더에 포함되서 보내짐.
          .then(()=> {
            // if (response.data.status === "success") {
            console.log("11111111111" + localStorage.getItem('token'));
            this.$router.push({ name:"userForm" });
            //commit : 변이를 수행한다. 페이로드값을 매개변수로. state에 저장한다.

          })
          .catch(()=>{
            //payload.callback({ status:"fail", message:"로그인 실패222 : " + error});
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
