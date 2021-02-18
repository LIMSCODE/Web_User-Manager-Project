package com.onlinepowers.springmybatis.user;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    public List<UserDto> getUserList() ;
    public UserDto getUser(Integer id);
    public void deleteUser(int id);
    public void insertUser(UserDto user);
    public void updateUser(UserDto user);

}
