package com.onlinepowers.springmybatis.user;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	int getCountByParam(User user);
	List<User> getUserList(User user);
	User getUserById(long id);
	void deleteUserById(long id);

	void insertUser(User user);
	void insertUserDetail(UserDetail userDetail);

	void updateUser(User user);
	void updateUserDetail(User user);

	int getUserCountByLoginId(String loginId);
	String getPasswordById(long id);

}