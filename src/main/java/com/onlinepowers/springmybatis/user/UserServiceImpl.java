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
    public List<User> getUserList(User user) {

        List<User> userList = Collections.emptyList();
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
    public User getUser(Integer id) { return userMapper.getUser(id); }
    @Override
    public void deleteUser(int id) {
        userMapper.deleteUser(id);
    }

    @Transactional
    @Override
    public void insertUser(User user, UserDetail userDetail) {
        userMapper.insertUser(user);
        userMapper.insertUserDetail(userDetail);
    }

    @Transactional
    @Override
    public void updateUser(User user , UserDetail userDetail) {
        userMapper.updateUser(user);
        userMapper.updateUserDetail(userDetail);
    }

    @Override
    public int checkId(String loginId)  {
        int result = userMapper.checkId(loginId);
        return result;
    }

}