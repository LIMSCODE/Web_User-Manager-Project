package com.onlinepowers.springmybatis.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    List<User> getUserList(User user) ;
    User getUser(Integer id);
    void deleteUser(int id);

    void insertUser(User user, UserDetail userDetail);

    void updateUser(User user, UserDetail userDetail);
    int checkId(String loginId);


}