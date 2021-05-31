import axios from 'axios';
import Constant from '../components/Constant';
let BASEURL = Constant.BASEURL;

export default {

    [Constant.SET_USER_INFO] : (store, payload)=> {
        store.commit(Constant.SET_USER_INFO, { token: payload.token, userInfo: payload.userInfo })
    },

    [Constant.USER_LIST] : (store)=> {
        axios.get(`${BASEURL}/api/opmanager/user/ajax-list`,    //{params : {pageNumber: pageno}
            { headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})
            .then((response)=>{
                console.log("리스트 데이터 받아오는중=========");
                console.log(response.data.content);    //받아와진다.
                store.commit(Constant.USER_LIST, { todolist: response });       //받아온 데이터로 변이수행, 상태변경

            })
            .catch((error)=> {
                console.log("## 에러 : ", error);
            })
    },

    [Constant.EDIT_DETAIL] : (store) => {
        axios.get(`http://localhost:8080/api/user/edit-detail`,
        {headers:{Authorization : 'Bearer ' + localStorage.getItem('token')}})
            .then((response)=>{
                store.commit(Constant.EDIT_DETAIL , { userDetail: response});
                console.log(response);

            })
            .catch((error)=>{
                console.log("=======수정 정보 불러오기 실패 : ", error);
            })
    },

/*
    [Constant.SEARCH_CONTACT] : (store, payload)=> {   //Constant.BASE_URL + payload.name)
        axios.get('http://localhost:8080/api/opmanager/user/ajax-list?searchKeyword=' + payload.searchKeyword + '&searchType=' + payload.searchType)
            .then((response)=> {
                store.commit(Constant.SEARCH_CONTACT, { contacts: response.data })
            })
    },
*/
}
