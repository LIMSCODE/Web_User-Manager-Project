package com.onlinepowers.springmybatis.security;
import com.onlinepowers.springmybatis.user.User;
import lombok.Data;
import org.springframework.security.core.authority.AuthorityUtils;

@Data
public class LoginUserDetails extends org.springframework.security.core.userdetails.User {        //UserDetails를 implement하는 시큐리티 커스텀

	private User user;

	public LoginUserDetails(User user) {
		super(user.getName(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getUserRole().getAuthority()));
		this.user = user;
	}

}

