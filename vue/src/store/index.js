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

export default store;
