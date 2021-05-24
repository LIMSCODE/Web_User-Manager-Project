import Vue from 'vue'
import App from './App.vue'
//import 'bootstrap/dist/css/bootstrap.css';
//import './main.css';
import VueRouter from 'vue-router';
import store from './store';
import Constant from './components/Constant';
import tokenutil, { getUserInfoFromToken, getToken } from './tokenutil';

import AddTodo from './components/AddTodo';
import UpdateTodo from './components/UpdateTodo';
import TodoList from './components/TodoList';
import Login from './components/Login';
import CreateUser from './components/CreateUser';
import Home from './components/Home';
import NotFound from './components/NotFound';
import AppHeader from './components/AppHeader';

import userMain from './components/main/user';
import userForm from './components/user/form';
import userLogin from './components/user/login';
import passwordCheck from './components/user/password-check';

import opmanagerMain from './components/opmanager';
import opmanagerForm from './components/opmanager/user/form';
import opmanagerList from './components/opmanager/user/list';
import opmanagerLogin from './components/opmanager/user/login';
import HelloWorld from './components/HelloWorld';
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

    //{ path:"/", name:"root", redirect: { name:"home" } },
    { path:"/home", name: "home", component : Home },
    { path:"/todolist", name:"todoList", component: TodoList },
    { path:"/todolist/add", name:"addTodo", component: AddTodo },
    { path:"/todolist/update/:id", name:"updateTodo", component: UpdateTodo },

    { path:"/login", name:"login", component:Login },
    { path:"/createuser", name:"createUser", component:CreateUser },
    { path:"*", component: NotFound },

    {path: '/', name : 'userMain', component : userMain},
    {path: '/user/create', name : 'userForm', component : userForm},
    {path: '/user/edit', name : 'userForm', component : userForm,
      children : [
        { path: ':no', name:'userForm', component: userForm, props:true }
      ]
    },
    {path: '/user/login', name : 'userLogin', component : userLogin},
    {path: '/user/password-check', name : 'passwordCheck', component : passwordCheck},

    {path: '/opmanager', name : 'opmanagerMain', component:opmanagerMain,
      beforeEnter: requireAuth()
    },
    {path: '/opmanager/user/list', name : 'opmanagerList', component:opmanagerList},
    {path: '/opmanager/user/login', name : 'opmanagerLogin', component:opmanagerLogin},

    {path: '/opmanager/user/create', name : 'opmanagerForm', component:opmanagerForm},
    {path: '/opmanager/user/edit', name : 'opmanagerForm', component : opmanagerForm,
      children : [
        { path: ':no', name:'opmanagerForm', component: opmanagerForm, props:true }
      ]
    },
    {path: '/h',name : 'HelloWorld', component:HelloWorld}
  ]
})

//opmanager 로 접속시 토큰있으면 opmanagerMain으로, 없으면 opmanagerLogin으로 라우팅
//router.beforeEach(() => {
//   let token = getToken();
//   let userInfo = getUserInfoFromToken();
//   if (token !== store.state.token) {
//     store.dispatch(Constant.SET_USER_INFO, { userInfo, token })
//   }

    //if (to.name === "login" && userInfo && userInfo.users_id) {
      //이미 로그인한 상태에서 login페이지로 접근하면 home으로..
    // if (to.name === "opmanagerMain"  && userInfo) {//&& userInfo&& userInfo.users_id
    //   next({ name : "opmanagerMain" })
    // }
    //else {     //로그인이 안된상태라면,
    //   //next({ name : "opmanagerLogin" })
    // }

  // } else {
  //   //가능하다면 role 정보를 이용해 접근 권한을 부여하도록 함.
  //   if (userInfo && userInfo.users_id) {
  //     next();    //그대로 이동한다.
  //   } else {
  //     next({ name:"login" })
  //   }
//});


new Vue({
  store,  //전역 스토어 생성, 하위컴포넌트에서 this.$store로 스토어 사용
  router,
  render: h => h(App),
}).$mount('#app')

