package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.paging.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public List<UserDto> getUserList(UserDto user) {

        List<UserDto> userList = Collections.emptyList();
        int userCount = userMapper.getCount(user);

        PaginationInfo paginationInfo = new PaginationInfo(user);
        paginationInfo.setTotalRecordCount(userCount);
        user.setPaginationInfo(paginationInfo);

        if (userCount > 0) {
            userList = userMapper.getUserList(user);
        }
        return userList;
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
