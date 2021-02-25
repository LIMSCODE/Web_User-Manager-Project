package com.onlinepowers.springmybatis.user;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	int getCount(User user);
	List<User> getUserList(User user);
	public User getUser(Integer id);
	public void deleteUser(int id);
	public void insertUser(User user);
	public void insertUserDetail(UserDetail userDetail);

	public void updateUser(User user);
	public void updateUserDetail(UserDetail userDetail);

	public int checkId(String loginId);

}