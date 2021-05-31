<template>
  <form v-on:submit.prevent="submitForm(getId)" id="target">
    <div>
      <label for="password">PW:</label>
      <input id="password" type="password" v-model="password"  />
    </div>
    <button type="submit">수정으로 이동</button>
  </form>
</template>

<script>
import axios from "axios";
import Constant from "@/components/Constant";
export default {
  user:function(){
    return{
      password : ''
    }
  },

  computed : {
    token() {
      return this.$store.state.token;
    },
    isToken : function() {
      return this.$store.state.token;
    },
    getId : function() {
      return this.$store.state.userDetail.userDetail.data.id;   //data undefined뜸
    }
  },

  mounted() {
    this.$store.dispatch(Constant.EDIT_DETAIL, {token: this.$route.token});
  },

  methods:{
    submitForm:function(id){
      var user = {
        password: this.password,
      }
      let form = new FormData();
      form.append('password', user.password);

      axios.post(`http://localhost:8080/api/user/password-check`, form,
          {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})  // 헤더에 포함되서 보내짐.
          .then(()=> {
            this.$router.push({ name:"userEditForm" , params : {"id" : id}});
          })
          .catch(()=>{
          })
    }
  }
};
</script>
