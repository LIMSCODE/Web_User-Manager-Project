package com.onlinepowers.springmybatis.jwt;

import lombok.Data;

@Data
public class JwtRequest {       //사용자에게 받은 아이디, 패스워드 저장

	private String loginId;
	private String password;
}
