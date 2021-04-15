package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public interface UserService {

    /**
     * 회원 삭제
     * @param id
     */
    void deleteUserById(long id);


    /**
     * 회원 등록
     * @param user
     */
    void insertUser(User user);


    /**
     * 회원정보 수정
     * @param user
     */
    void updateUser(User user);


    /**
     * 로그인시 회원정보 가져오기 (세션저장 등)
     * @param loginId
     * @return
     */
    User getUserByLoginId(String loginId);


    /**
     * 회원 insert시 비밀번호 암호화에 사용되는 PK (최대 PK값 + 1)
     * @return
     */
    long getMaxPK();


    /**
     * 아이디 중복 확인시 해당 아이디 몇개인지
     * @param loginId
     * @return
     */
    int getUserCountByLoginId(String loginId);


    /**
     * ID로 회원정보 가져오기 (수정시)
     * @param id
     * @return
     */
    User getUserById(long id);


    /**
     * 회원 목록
     * @param user
     * @param jpaPaging
     * @return
     */
    Page<User> getUserList(User user, Pageable pageable, @ModelAttribute("jpaPaging") JpaPaging jpaPaging) ;



}