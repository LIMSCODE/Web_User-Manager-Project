package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface UserService {

    public List<UserDto> getUserList(UserDto user) ;
    public UserDto getUser(Integer id);
    public void deleteUser(int id);
    public void insertUser(UserDto user);
    public void updateUser(UserDto user);
    public int idCheck(String loginId);


}
