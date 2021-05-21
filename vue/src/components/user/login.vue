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
  </form>
</template>

<script>
import axios from 'axios';
export default {
  user:function(){
    return{
      loginId : '',
      password : ''
    }
  },
  methods:{
    submitForm:function(){
      console.log(this.loginId, this.password);
      var url = 'http://localhost:8080/api/user/login';
      var user = {
        loginId: this.loginId,
        password: this.password,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json; charset = utf-8'
        }
      }
      let form = new FormData()
      form.append('loginId', user.loginId);
      form.append('password', user.password);
      axios.post(url, form, { useCredentails: true })
          .then(function(token){
            console.log(token);
            localStorage.setItem('wtw-token', token);
            window.location.href = "/";
          })
          .catch(function(response){
            console.log(response);
          });
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
