package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserMapper userMapper;

    @Override
    public int getCountByParam(User user){

        int userCount = userMapper.getCountByParam(user);
        return userCount;
    }

    @Override
    public List<User> getUserList(User user) {

        List<User> userList = Collections.emptyList();
        int userCount = userMapper.getCountByParam(user);

        PaginationInfo paginationInfo = new PaginationInfo(user);
        paginationInfo.setTotalRecordCount(userCount);
        user.setPaginationInfo(paginationInfo);

        if (userCount > 0) {
            userList = userMapper.getUserList(user);
        }
        return userList;
    }

    @Override
    public User getUserById(long id) { return userMapper.getUserById(id); }
    @Override
    public void deleteUserById(long id) {
        userMapper.deleteUserById(id);
    }

    @Transactional
    @Override
    public void insertUser(User user) {
        userMapper.insertUser(user);
        userMapper.insertUserDetail(user.userDetail);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
        userMapper.updateUserDetail(user);
    }

    @Override
    public int getUserCountByLoginId(String loginId)  {
        int userCount = userMapper.getUserCountByLoginId(loginId);
        return userCount;
    }

}