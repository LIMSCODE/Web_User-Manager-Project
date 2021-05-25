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
  mounted() {     //수정정보 불러오기, 첫창에 하면 안되는것 수정 LJY
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
            //computed로 상태로부터 계산한 id값을 템플릿의 메서드에서 매개변수로 updateTodo함수에 전달한다.
            //이렇게하면 주소창에 /id값 뜬다. (라우터에서 :id연결)
            //commit : 변이를 수행한다. 페이로드값을 매개변수로. state에 저장한다.
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
