package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Criteria {

	private long id;
	private long pagingId;
	private String name;
	private String loginId;
	private String password;
	private String salt;
	private String email;
	private String createdDate;

	public UserDetail userDetail;
	public UserRole userRole;

}