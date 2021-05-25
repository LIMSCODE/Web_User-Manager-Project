import Vue from 'vue';
import Constant from '../components/Constant';

export default {
    [Constant.SEARCH_CONTACT] : (state, payload) => {
        state.contacts = payload.contacts;
    },

    [Constant.SET_USER_INFO] : (state, payload)=> {
        state.userInfo = payload.userInfo;
        state.token = payload.token;    //state의 상태를 바꾸는 변이.
    },

    [Constant.LOAD_TODOLIST] : (state, payload)=> {
        state.todolist = payload.todolist;
    },
    [Constant.ADD_TODO] :(state,payload)=> {
        state.todolist.push({ ...payload.todoitem });
        state.todoitem = { id:"", todo:"", desc:"", done:false };
    },
    [Constant.DELETE_TODO] :(state,payload)=> {
        let index = state.todolist.findIndex((item)=>item.id === payload.id);   //payload에서 id전달받아서 그 번호에 넣음
        state.todolist.splice(index,1);
    },
    [Constant.UPDATE_TODO] :(state,payload)=> {
        let index = state.todolist.findIndex((item)=>item.id === payload.todoitem.id);
        Vue.set(state.todolist, index, payload.todoitem);
    },

    [Constant.INITIALIZE_TODOITEM] :(state,payload)=> {
        if (payload && payload.todoitem) {
            state.todoitem = payload.todoitem;
        } else {
            state.todoitem = { id:"", todo:"", desc:"", done:false };
        }
    },
    [Constant.EDIT_DETAIL] :(state,payload)=> {
        state.userDetail = payload;
    },

    [Constant.CHANG_ISLOADING] : (state, payload)=> {
        state.isloading = payload.isloading;
    }
}
