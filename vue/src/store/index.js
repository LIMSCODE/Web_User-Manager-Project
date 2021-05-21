import Vue from 'vue';
import Vuex from 'vuex';

import state from './state';
import mutations from './mutations';
import actions from './actions';

Vue.use(Vuex);

// state, mutation, action 정보를 전달하여 Vuex.Store객체를 생성
const store = new Vuex.Store({
    strict : process.env.NODE_ENV !== 'production',
    state,
    mutations,
    actions
})

export default store;
