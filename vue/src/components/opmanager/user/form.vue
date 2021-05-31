<template>
  <div class="container">
    <div v-if="$route.params.id">   <!--관리자에서 회원수정 : 라우터에 파람값 있을때는 수정창으로 뜨도록-->
      <div>관리자- 회원 수정</div>
      <form @submit.prevent="editUsers">
        <div>
          <label for="loginId">아이디</label>
          <input type="text" id="loginId" name="loginId" v-model="editDetail.loginId" />
        </div>
        <div>
          <label for="name">성함</label>
          <input type="text" id="name" name="name" v-model="editDetail.name" />
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
          <input type="text" id="email" name="email" v-model="editDetail.email" />
        </div>
        <div>
          <label for="zipcode">우편번호</label>
          <input type="text" id="zipcode" name="zipcode" v-model="editDetail.userDetail.zipcode" />
        </div>
        <div>
          <label for="address">주소</label>
          <input type="text" id="address" name="address" v-model="editDetail.userDetail.address" />
        </div>
        <div>
          <label for="addressDetail">상세주소</label>
          <input type="text" id="addressDetail" name="addressDetail" v-model="editDetail.userDetail.addressDetail" />
        </div>
        <div>
          <label for="phoneNumber">전화번호</label>
          <input type="text" id="phoneNumber" name="phoneNumber" v-model="editDetail.userDetail.phoneNumber" />
        </div>
        <!--DB에서 sms, 롤 정보 불러와서 라디오버튼에 체크되도록 해야함 LJY-->
        <div>
          <label>Sms수신여부</label>
          수신<input type="radio" name="receiveSms" v-model="receiveSms" value="1" label="수신" />
          수신안함<input type="radio" name="receiveSms" v-model="receiveSms" value="0" label="수신x" />
        </div>
        <span>
          <input type="hidden" name="authority" id="authority" value="ROLE_USER" >
        </span>
        <button type="submit">수정 완료</button>
      </form>
    </div>

    <div v-else>
      <div>관리자- 회원등록</div>
      <form @submit.prevent="createUsers" >
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
          수신<input type="radio" name="receiveSms" v-model="receiveSms" value="1" label="수신" />
          수신안함<input type="radio" name="receiveSms" v-model="receiveSms" value="0" label="수신x" />
        </div>
        <div>
          <label>권한</label>
          회원<input type="radio" name="authority" v-model="authority" value="ROLE_USER" label="회원" />
          관리자<input type="radio" name="authority" v-model="authority" value="ROLE_OPMANAGER" label="관리자" />
          <!--회원등록시 관리자로했는데 유저로 등록됨 LJY-->
        </div>
        <button type="submit">회원가입</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Constant from "@/components/Constant";

export default {
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
      receiveSms:'',
      id : this.$router.params.id,
      // edits : {}
    }
  },

  mounted() {     //수정정보 불러오기
    if(this.user.id) {
      this.$store.dispatch(Constant.EDIT_DETAIL, {token: this.$route.token});
      // this.edits = { ...this.editDetail };
    }
  },

  computed : {
    isToken : function() {
      return this.$store.state.token;
    },
    editDetail : function()  {
      return this.$store.state.userDetail.userDetail.data;
    }
  },

  methods : {
    createUsers : function(){
      var url = 'http://localhost:8080/api/opmanager/user/create';
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
        authority : this.authority
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
      form.append('userRole.authority', user.authority);   //바로 유저로 만듬

      axios.post(url, form, {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})
          .then(function(response){
            console.log(response);
            window.location.href = '/opmanager/user/list';
          })
          .catch(function(response){
            console.log(response);
          });
    },

    editUsers : function(){
      var url = 'http://localhost:8080/api/opmanager/user/edit/' + this.router.params.id;
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
        authority : this.editDetail.userRole.authority
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
      form.append('userRole.authority', user.authority);

      axios.post(url, form, {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})
          .then(function(response){
            console.log(response);
            window.location.href = '/opmanager/user/list';
          })
          .catch(function(response){
            console.log(response);
          });
    }
  }
};
</script>
