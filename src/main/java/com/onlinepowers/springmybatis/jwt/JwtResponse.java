package com.onlinepowers.springmybatis.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data        //사용자에게 반환될 JWT 담은 Response
@AllArgsConstructor
public class JwtResponse {

	private String token;
}
