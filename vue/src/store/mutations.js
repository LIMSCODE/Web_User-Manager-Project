import Constant from '../components/Constant';

export default {

    [Constant.SET_USER_INFO] : (state, payload)=> {
        state.userInfo = payload.userInfo;
        state.token = payload.token;    //state의 상태를 바꾸는 변이.
    },

    [Constant.USER_LIST] : (state, payload)=> {
        console.log("USER_LIST");
        state.todolist = payload.todolist;
    },

    [Constant.EDIT_DETAIL] :(state,payload)=> {
        state.userDetail = payload;
    }

/*
    [Constant.SEARCH_CONTACT] : (state, payload) => {
        state.contacts = payload.contacts;
    },
*/
}
