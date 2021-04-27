package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

@Service
public interface UserService extends UserDetailsService {

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
    Page<User> getUserList(User user, Pageable pageable, @ModelAttribute("jpaPaging") JpaPaging jpaPaging);




}