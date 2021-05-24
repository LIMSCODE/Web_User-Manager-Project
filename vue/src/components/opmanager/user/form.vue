<template>
  <div class="container">
    <form @submit.prevent="SignupForm" >
      <div>
        <label for="loginId">아이디</label>
        <input type="text" id="loginId" name="loginId" v-model="loginId" />
      </div>
      <div>
        <label for="name">성함</label>
        <input type="text" id="name" name="name" v-model="name" />
      </div>
      <div>
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" v-model="password" />
      </div>
      <div>
        <label for="passwordConfirm">비밀번호 확인</label>
        <input type="password" id="passwordConfirm" name="passwordConform" v-model="passwordConfirm" />
      </div>
      <div>
        <label for="email">email</label>
        <input type="text" id="email" name="email" v-model="email" />
      </div>
      <div>
        <label for="zipcode">우편번호</label>
        <input type="text" id="zipcode" name="zipcode" v-model="zipcode" />
      </div>
      <div>
        <label for="address">주소</label>
        <input type="text" id="address" name="address" v-model="address" />
      </div>
      <div>
        <label for="addressDetail">상세주소</label>
        <input type="text" id="addressDetail" name="addressDetail" v-model="addressDetail" />
      </div>
      <div>
        <label for="phoneNumber">전화번호</label>
        <input type="text" id="phoneNumber" name="phoneNumber" v-model="phoneNumber" />
      </div>

      <div>
        <label>Sms수신여부</label>
        수신<input type="radio" name="receiveSms"
                 v-model="receiveSms" value="1" label="수신" />
        수신안함<input type="radio" name="receiveSms"
                   v-model="receiveSms" value="0" label="수신x" />
      </div>
      <span>
        <input type="hidden" name="authority" id="authority"
               value="ROLE_USER" >
      </span>

      <button type="submit">회원가입</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  props : ['id'],
  user:function(){
    return{
      loginId: '',
      name: '',
      password: '',
      email:'',
      zipcode:'',
      address:'',
      addressDetail:'',
      phoneNumber:'',
      receiveSms:''
    }
  },

  methods:{
    SignupForm:function(){
      console.log(this.loginId, this.password);
      var url = 'http://localhost:8080/api/user/create';
      var user = {
        loginId: this.loginId,
        name : this.name,
        password: this.password,
        email : this.email,
        zipcode : this.zipcode,
        address : this.address,
        addressDetail:this.addressDetail,
        phoneNumber : this.phoneNumber,
        receiveSms : this.receiveSms,
        headers: {
          'Access-Control-Allow-Origin': '*',
          'Content-Type': 'application/json; charset = utf-8',
        }
      }
      let form = new FormData();
      form.append('loginId', user.loginId);
      form.append('name', user.name);
      form.append('password', user.password);
      form.append('email', user.email);
      form.append('userDetail.zipcode', user.zipcode);
      form.append('userDetail.address', user.address);
      form.append('userDetail.addressDetail', user.addressDetail);
      form.append('userDetail.phoneNumber', user.phoneNumber);
      form.append('userDetail.receiveSms', user.receiveSms);
      form.append('userRole.authority', 'ROLE_USER');   //바로 유저로 만듬
      axios.post(url, form)
          .then(function(response){
            console.log(response);
            window.location.href = "/";
          })
          .catch(function(response){
            console.log(response);
          });
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
