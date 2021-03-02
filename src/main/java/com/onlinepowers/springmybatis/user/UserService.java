package com.onlinepowers.springmybatis.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    int getCountByParam(User user);
    List<User> getUserList(User user) ;
    User getUserById(long id);

    void deleteUserById(long id);
    void insertUser(User user);
    void updateUser(User user);
    int getUserCountByLoginId(String loginId);


}