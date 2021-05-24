<template>
  <form v-on:submit.prevent="submitForm(getId)" id="target">
    <div>
      <label for="password">PW:</label>
      <input id="password" type="password" v-model="password"  />
    </div>
    {{ getId }}
    <button type="submit">수정으로 이동</button>
    {{ isToken }}

  </form>
</template>

<script>
// import axios from 'axios';
// import {getUserInfoFromToken} from "@/tokenutil";
//import Constant from "@/components/Constant";
import axios from "axios";
import Constant from "@/components/Constant";
//import {getUserInfoFromToken} from "@/tokenutil";
export default {
  user:function(){
    return{
      loginId : '',
      password : ''
    }
  },
  computed : {
    userid() {
      return this.$store.state.todoitem;
    },
    token() {
      return this.$store.state.token;
    },
    isToken : function() {
      return this.$store.state.token;
    },
    getId : function() {
      return this.$store.state.userDetail.userDetail.data.id;
    }
  },
  mounted() {     //수정정보 불러오기
    this.$store.dispatch(Constant.EDIT_DETAIL, {token: this.$route.token});
  },
  methods:{
    submitForm:function(id){

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
      console.log(localStorage.getItem('token'));   //토큰 가져와짐.

      // this.$store.dispatch(Constant.LOGIN, {form})
      axios.post(`http://localhost:8080/api/user/password-check`, form,
          {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})  // 헤더에 포함되서 보내짐.
          .then(()=> {
            // if (response.data.status === "success") {
            console.log(localStorage.getItem('token'));

            this.$router.push({ name:"userForm" , params : {"id" : id}});
            //computed로 상태로부터 계산한 id값을 템플릿의 메서드에서 매개변수로 updateTodo함수에 전달한다.
            //이렇게하면 주소창에 /id값 뜬다. (라우터에서 :id연결)

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
