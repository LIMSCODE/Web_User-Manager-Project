package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface UserMapper {

	int getCount(UserDto user);
	List<UserDto> getUserList(UserDto user);
	public UserDto getUser(Integer id);
	public void deleteUser(int id);
	public void insertUser(UserDto user);
	public void updateUser(UserDto user);
	public int idCheck(String loginId);

}
