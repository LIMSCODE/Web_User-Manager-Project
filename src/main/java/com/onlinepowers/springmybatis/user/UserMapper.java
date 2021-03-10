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
	void insertUserRole(UserRole userRole);

	void updateUser(User user);
	void updateUserDetail(User user);
	void updateUserRole(User user);

	int getUserCountByLoginId(String loginId);
	String getPasswordById(long id);

	String getSaltById(long id);
	
	
	//로그인
	User getUserByLoginId(String loginId);



}