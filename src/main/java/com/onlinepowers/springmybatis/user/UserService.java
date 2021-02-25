package com.onlinepowers.springmybatis.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    public List<User> getUserList(User user) ;
    public User getUser(Integer id);
    public void deleteUser(int id);

    public void insertUser(User user, UserDetail userDetail);

    public void updateUser(User user, UserDetail userDetail);
    public int checkId(String loginId);


}