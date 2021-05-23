import axios from 'axios';
import Constant from '../components/Constant';
import { getUserInfoFromToken } from '../tokenutil';

let BASEURL = Constant.BASEURL;

export default {
    [Constant.LOGIN] : (store, payload) => {
        let { loginId, password } = payload;
        axios.post(`${BASEURL}/api/user/login`, { loginId, password })
            .then((token)=> {
               // if (response.data.status === "success") {

                    window.localStorage.setItem("token", token);    //컨트롤러에서 200뜸
                    console.log("로그인액션=============="+token);

                   const userInfo = getUserInfoFromToken();
                   store.commit(Constant.SET_USER_INFO, { token, userInfo })
                    //commit : 변이를 수행한다. 페이로드값을 매개변수로. state에 저장한다.
                       // payload.callback(token);
                // } else {
                //     payload.callback(respoㅇnse.data);
                // }
            })
            .catch((error)=>{
                payload.callback({ status:"fail", message:"로그인 실패222 : " + error});
            })
    },
    [Constant.SET_USER_INFO] : (store, payload)=> {
        store.commit(Constant.SET_USER_INFO, { userInfo: payload.userInfo, token: payload.token })
    },

    [Constant.CREATE_USER] : (store, payload) => {
        let { id, password, username } = payload;
        axios.post(`${BASEURL}/api/user/create`, {
            id, password, username
        })
            .then((response)=> {
                payload.callback(response.data);
            })
            .catch((error)=> {
                console.log("## 사용자 생성 실패 : " + error);
                payload.callback({ message: "사용자 계정 생성 실패", status:"fail" });
            })
    },

    [Constant.ADD_TODO] : (store, payload) => {
        let { todo, desc } = payload.todoitem;
        store.commit(Constant.CHANG_ISLOADING, { isloading: true })
        axios.post(`${BASEURL}/todolist`, { todo, desc }, {
            headers: { Authorization: "Bearer " + store.state.token }
        })
            .then((response)=>{
                if (response.data.status === "success") {
                    payload.todoitem.id = response.data.todo.id;
                    store.commit(Constant.ADD_TODO, payload);
                } else {
                    console.log("할일 추가 실패 : ", response.data.message);
                }
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
            .catch((error)=>{
                console.log("할일 추가 실패 : ", error);
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
    },
    [Constant.DELETE_TODO] : (store, payload) => {
        store.commit(Constant.CHANG_ISLOADING, { isloading: true })
        axios.delete(`${BASEURL}/todolist/${payload.id}`, {
            headers: { Authorization: "Bearer " + store.state.token }
        })
            .then((response)=>{
                if (response.data.status === "success") {
                    store.commit(Constant.DELETE_TODO, payload);
                } else {
                    console.log("할일 삭제 실패 : ", response.data.message);
                }
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
            .catch((error)=>{
                console.log("할일 삭제 실패 : ", error);
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
    },
    [Constant.TOGGLE_DONE] : (store, payload) => {
        store.commit(Constant.CHANG_ISLOADING, { isloading: true })
        axios.put(`${BASEURL}/todolist/${payload.id}/done`, {}, {
            headers: { Authorization: "Bearer " + store.state.token }
        })
            .then((response)=>{
                if (response.data.status === "success") {
                    store.commit(Constant.TOGGLE_DONE, payload);
                } else {
                    console.log("할일 완료 변경 실패 : ", response.data.message);
                }
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
            .catch((error)=>{
                console.log("할일 완료 변경 실패 : ", error);
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
    },
    [Constant.UPDATE_TODO] : (store, payload) => {
        store.commit(Constant.CHANG_ISLOADING, { isloading: true })
        let { id, todo, desc, done } = payload.todoitem;
        axios.put(`${BASEURL}/todolist/${id}`, { todo, desc, done }, {
            headers: { Authorization: "Bearer " + store.state.token }
        })
            .then((response)=>{
                if (response.data.status === "success") {
                    store.commit(Constant.UPDATE_TODO, payload);
                } else {
                    console.log("할일 완료 변경 실패 : ", response.data.message);
                }
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
            .catch((error)=>{
                console.log("할일 완료 변경 실패 : ", error);
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
    },
    [Constant.INITIALIZE_TODOITEM] : (store, payload) => {
        store.commit(Constant.INITIALIZE_TODOITEM, payload);
    },
    [Constant.LOAD_TODOLIST] : (store)=> {
        store.commit(Constant.CHANG_ISLOADING, { isloading: true })
        axios.get(`${BASEURL}/todolist`, {
            headers: { Authorization: "Bearer " + store.state.token }
        })
            .then((response)=>{
                store.commit(Constant.LOAD_TODOLIST, { todolist: response.data.todolist });
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
            .catch((error)=> {
                console.log("## 에러 : ", error);
                store.commit(Constant.CHANG_ISLOADING, { isloading: false })
            })
    }

}
