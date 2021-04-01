package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.List;

@Service
public interface UserService {

    /**
     * 검색된 회원수
     * @param user
     * @return
     */
    int getCountByParam(User user);

    /**
     * 회원 목록
     * @param user
     * @param cri
     * @return
     */
    List<User> getUserList(User user,  @ModelAttribute("cri") Criteria cri) ;

    /**
     * ID로 회원정보 가져오기
     * @param id
     * @return
     */
    User getUserById(long id);

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
     * 페이지 넘버링 (최대 PK값 + 1)
     * @return
     */
    int getMaxPk();

    /**
     * 아이디 중복 확인시 해당 아이디 몇개인지
     * @param loginId
     * @return
     */
    int getUserCountByLoginId(String loginId);

    /**
     * 로그인시 회원정보 가져오기
     * @param loginId
     * @return
     */
    User getUserByLoginId(String loginId);

}