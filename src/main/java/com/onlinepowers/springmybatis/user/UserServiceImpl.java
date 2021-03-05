package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.paging.PaginationInfo;
import com.onlinepowers.springmybatis.util.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public int getCountByParam(User user){

        int userCount = userMapper.getCountByParam(user);

        return userCount;
    }

    @Override
    public List<User> getUserList(User user, @ModelAttribute("cri") Criteria cri) {

        List<User> userList = Collections.emptyList();
        int userCount = userMapper.getCountByParam(user);

        PaginationInfo paginationInfo = new PaginationInfo(user);
        paginationInfo.setTotalRecordCount(userCount);
        user.setPaginationInfo(paginationInfo);

        if (userCount > 0) {
            userList = userMapper.getUserList(user);
            Criteria criteria = new Criteria();

            int currentPageNo = cri.getCurrentPageNo(); //현재 페이지
            int totalRecordCount = userMapper.getCountByParam(user);    // 전체 데이터 개수
            int recordsPerPage = criteria.getRecordsPerPage();  //한페이지에 들어가는 데이터 개수
            int totalPageCount = (totalRecordCount ) / recordsPerPage + 1;   // 전체 페이지 개수

            for (int i = 0 ; i < userList.size(); i++) {

                // 해당 페이지에서 가장 큰 번호 구하기
                int start = totalRecordCount - ( currentPageNo - 1) * recordsPerPage ;
                // 하나씩 빼서 게시글을 뿌린다.
                int num = start - i;

                userList.get(i).setPagingId(num);
            }
        }

        return userList;
    }

    @Override
    public User getUserById(long id) { return userMapper.getUserById(id); }
    @Override
    public void deleteUserById(long id) {
        userMapper.deleteUserById(id);
    }

    @Transactional
    @Override
    public void insertUser(User user) {

        String salt = SHA256Util.generateSalt();
        user.setSalt(salt);

        String password = user.getUserPw();
        password = SHA256Util.getEncrypt(password, salt);
        user.setUserPw(password);

        userMapper.insertUser(user);
        userMapper.insertUserDetail(user.userDetail);

    }

    @Transactional
    @Override
    public int updateUser(User user) {

        String salt = userMapper.getSaltById(user.getId());
        log.debug(salt);
        String password = user.getUserPw();
        log.debug(password);
        String hashPassword = SHA256Util.getEncrypt(password, salt);
        log.debug(hashPassword);

        //해시함수로 가져온 것과 입력한것을 해시함수로 변환한 것이 일치하면 수정한다.
        if (hashPassword.equals (userMapper.getPasswordById(user.getId())) ) {

            user.setUserPw(hashPassword);
            userMapper.updateUser(user);
            userMapper.updateUserDetail(user);

            return 1;   //컨트롤러에서 받는 값

        } else {
            log.debug("비밀번호 일치하지 않음");

            return 0;
        }
    }

    @Override
    public int getUserCountByLoginId(String loginId)  {
        int userCount = userMapper.getUserCountByLoginId(loginId);
        return userCount;
    }

}