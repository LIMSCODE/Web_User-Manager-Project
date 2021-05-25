import Vue from 'vue'
import App from './App.vue'
//import 'bootstrap/dist/css/bootstrap.css';
//import './main.css';
import VueRouter from 'vue-router';
import store from './store';
import Constant from './components/Constant';
import tokenutil, { getUserInfoFromToken, getToken } from './tokenutil';

import TodoList from './components/TodoList';
import AppHeader from "@/components/AppHeader";

import userMain from './components/main/user';
import userForm from './components/user/form';
import userLogin from './components/user/login';
import passwordCheck from './components/user/password-check';

import opmanagerMain from './components/opmanager';
import opmanagerForm from './components/opmanager/user/form'
import opmanagerList from './components/opmanager/user/list';
import opmanagerLogin from './components/opmanager/user/login';

tokenutil();
Vue.component("AppHeader", AppHeader);
Vue.use(VueRouter);
Vue.config.productionTip = false

let token = getToken();
let userInfo = getUserInfoFromToken();
if (token !== store.state.token) {
  store.dispatch(Constant.SET_USER_INFO, { userInfo, token })
}

const requireAuth = () => (to, from, next) => {
  if (store.state.token !== '') {
    return next();
  }
  next('/opmanager/user/login');    //로그인x로 /opmanager접속시 로그인창으로보냄
};

const router = new VueRouter({
  mode : "history",
  routes : [
    {path: '/', name : 'userMain', component : userMain},
    {path: '/user/create', name : 'userCreateForm', component : userForm},
    {path: "/user/edit/:id", name : "userEditForm", component : userForm},
    {path: '/user/login', name : 'userLogin', component : userLogin},
    {path: '/user/password-check', name : 'passwordCheck', component : passwordCheck},

    {path: '/opmanager', name : 'opmanagerMain', component:opmanagerMain, beforeEnter: requireAuth()},
    {path: '/opmanager/user/list', name : 'opmanagerList', component:opmanagerList, beforeEnter: requireAuth()},
    {path: '/opmanager/user/login', name : 'opmanagerLogin', component:opmanagerLogin},

    {path: '/opmanager/user/create', name : 'opmanagerForm1', component:opmanagerForm},
    {path: '/opmanager/user/edit:id', name : 'opmanagerForm2', component : opmanagerForm},
      // children : [
      //   { path: ':no', name:'opmanagerForm', component: opmanagerForm, props:true }
      // ]
    {path:"/todolist", name:"todoList", component: TodoList},
  ]
})


/*
    router.beforeEach(() => {
      let token = getToken();
      let userInfo = getUserInfoFromToken();
      if (token !== store.state.token) {
        store.dispatch(Constant.SET_USER_INFO, { userInfo, token })
      }

    // opmanager 로 접속시 토큰있으면 opmanagerMain으로, 없으면 opmanagerLogin으로 라우팅
    if (to.name === "login" && userInfo && userInfo.users_id) {
        if (to.name === "opmanagerMain"  && userInfo) {   //&& userInfo&& userInfo.users_id
            next({ name : "opmanagerMain" })
        }
        else {
          next({ name : "opmanagerLogin" })
        }

    } else {
      // 로그인안된상태이면 로그인창으로
      if (userInfo && userInfo.users_id) {
        next();    // 그대로 이동한다.
      } else {
        next({ name:"login" })  
      }
    });
*/

new Vue({
  store,      //전역 스토어 생성, 하위컴포넌트에서 this.$store로 스토어 사용
  router,
  render: h => h(App),
}).$mount('#app')

