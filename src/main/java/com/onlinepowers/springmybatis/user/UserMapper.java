package com.onlinepowers.springmybatis.user;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	int getCount(User user);
	List<User> getUserList(User user);
	User getUser(Integer id);
	void deleteUser(int id);
	void insertUser(User user);
	void insertUserDetail(UserDetail userDetail);

	void updateUser(User user);
	void updateUserDetail(UserDetail userDetail);

	int checkId(String loginId);

}