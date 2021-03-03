package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Service
public interface UserService {

    int getCountByParam(User user);
    List<User> getUserList(User user,  @ModelAttribute("cri") Criteria cri) ;
    User getUserById(long id);

    void deleteUserById(long id);
    void insertUser(User user);
    void updateUser(User user);
    int getUserCountByLoginId(String loginId);


}