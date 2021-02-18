package com.onlinepowers.springmybatis.user;

import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	int getCount();
	List<UserDto> getUserList();
	public UserDto getUser(Integer id);
	public void deleteUser(int id);
	public void insertUser(UserDto user);
	public void updateUser(UserDto user);

}
