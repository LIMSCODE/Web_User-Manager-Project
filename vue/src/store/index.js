import Vue from 'vue';
import Vuex from 'vuex';

import state from './state';
import mutations from './mutations';
import actions from './actions';
import getters from './getters';

Vue.use(Vuex);

// state, mutation, action 정보를 전달하여 Vuex.Store객체를 생성
const store = new Vuex.Store({
    strict : process.env.NODE_ENV !== 'production',
    state,      //관리할 상태
    mutations,  //상태를 업데이트 (상태, 페이로드)
    actions,
    getters
})

//commit : 액션에서 변이를 수행한다. 페이로드값을 매개변수로. state에 불러온 리스트 데이터를 저장한다.
//store.commit(Constant.LOAD_TODOLIST, { todolist: response.data.content });

//dispatch : 컴포넌트에서 액션을 수행한다.
//this.$store.dispatch(Constant.EDIT_DETAIL, {token: this.$route.token});

//state : 컴포넌트에서 state 상태정보 받아온다.
//return this.$store.state.token;

//컴프넌트에서 경로 이동하기
//this.$router.push({ name: 'managerEditForm', params: { "id" : id } })     //id값이 전달된다.


export default store;
