<template>
  <div class="container">
    <div v-if="isToken">        <!--토큰있을때 수정창으로 뜨도록-->
    <form @submit.prevent="editForm1" >
      <div>
      User일때 수정
      </div>
      <div>
        <label for="loginId">아이디</label>      <!-- v-model="loginId"-->
        <input type="text" id="loginId" name="loginId" disabled v-model="editDetail.loginId"/>  <!--: value 앞에 v-bind생략됨-->
      </div>
      <div>
        <label for="name">성함</label>
        <input type="text" id="name" name="name" v-model="editDetail.name" />
      </div>
      <div>
        <label for="password">비밀번호</label>
        <input type="password" id="password" name="password" v-model="password"  />
      </div>
      <div>
        <label for="passwordConfirm">비밀번호 확인</label>
        <input type="password" id="passwordConfirm" name="passwordConform" v-model="passwordConfirm" />
      </div>
      <div>
        <label for="email">email</label>
        <input type="text" id="email" name="email" v-model="editDetail.email" />
      </div>
      <div>
        <label for="zipcode">우편번호</label>
        <input type="text" id="zipcode" name="zipcode" v-model="editDetail.userDetail.zipcode" />
      </div>
      <div>
        <label for="address">주소</label>
        <input type="text" id="address" name="address" v-model="editDetail.userDetail.address"  />
      </div>
      <div>
        <label for="addressDetail">상세주소</label>
        <input type="text" id="addressDetail" name="addressDetail" v-model="editDetail.userDetail.addressDetail"/>
      </div>
      <div>
        <label for="phoneNumber">전화번호</label>
        <input type="text" id="phoneNumber" name="phoneNumber" v-model="editDetail.userDetail.phoneNumber" />
      </div>


      <span>
        <input type="hidden" name="receiveSms" id="receiveSms" value="0" >
      </span>
      <span>
        <input type="hidden" name="authority" id="authority" value="ROLE_USER" >
      </span>
      <button type="button" class="btn btn-default" @click="updateTodo(getId)">원래수정-값뜨는지확인</button>

      {{ getId }}   <!--8이라고 뜬다.-->
      <button type="submit">수정 완료</button>


      {{ editDetail }}
    </form>
  </div>

    <div v-else>
        <div>123123
        </div>
      {{ createId }}

      <form @submit.prevent="signupForm1" >
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
  </div>
</template>

<script>
import axios from "axios";
import Constant from "@/components/Constant";

export default {
  user : function(){
    return {
      loginId: '',
      name: '',
      password: '',
      email:'',
      zipcode:'',
      address:'',
      addressDetail:'',
      phoneNumber:'',
      receiveSms:'',
      id : this.$router.params.num,
      edits : {}
    }
  },
  mounted() {     //수정정보 불러오기
    if(this.id) {
      this.$store.dispatch(Constant.EDIT_DETAIL, {token: this.$route.token});
      this.edits = { ...this.editDetail };
    }
  },
  //watch는 computed나 data 속성이 변할때 같은이름으로 수행된다.
  watch : {   //회원가입 후 아이디 받아오는 속성,, computed가아닌 watched에 넣으면 되는가????

  },
  computed : {
    getId : function() {
      return this.$store.state.userDetail.userDetail.data.id;
    },
    isToken : function() {
      return this.$store.state.token;
    },
    createId : function() {
      return "111";
    },
    editDetail : function()  {
      return this.$store.state.userDetail.userDetail.data;
    }
  },

  methods:{
    signupForm1 :function(){
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
    },

    editForm1:function(){
      console.log(this.editDetail.loginId, this.editDetail.password);
      var url = 'http://localhost:8080/api/user/edit/' +this.editDetail.id;
      var user = {
        loginId: this.editDetail.loginId,
        name : this.editDetail.name,
        password: this.editDetail.password,
        email : this.editDetail.email,
        zipcode : this.editDetail.userDetail.zipcode,
        address : this.editDetail.userDetail.address,
        addressDetail:this.editDetail.userDetail.addressDetail,
        phoneNumber : this.editDetail.userDetail.phoneNumber,
        receiveSms : this.editDetail.userDetail.receiveSms,
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

      axios.post(url, form, {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})
          .then(function(response){
            console.log(response);
            this.$router.push({ name:"userMain"});
          })
          .catch(function(response){
            console.log(response);
          });
    },

    updateTodo(id) {
      console.log(id);
      this.$router.push({ name:"updateTodo" , params : {"id" : id}});
      //computed로 상태로부터 계산한 id값을 템플릿의 메서드에서 매개변수로 updateTodo함수에 전달한다.
      //이렇게하면 주소창에 /id값 뜬다. (라우터에서 :id연결)
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
