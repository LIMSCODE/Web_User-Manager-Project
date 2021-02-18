package com.onlinepowers.springmybatis.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserDto> getUserList() {
        return userMapper.getUserList();
    }
    @Override
    public UserDto getUser(Integer id) { return userMapper.getUser(id); }
    @Override
    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }

    @Override
    public void insertUser(UserDto user) {
        userMapper.insertUser(user);
    }
    @Override
    public void updateUser(UserDto user) {
        userMapper.updateUser(user);
    }
}
