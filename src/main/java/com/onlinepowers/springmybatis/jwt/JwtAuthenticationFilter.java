package com.onlinepowers.springmybatis.jwt;

import com.onlinepowers.springmybatis.user.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
	//요청이 들어올 때 마다 JWT를 인증할 Filter를 생성
	//Bearer로 토큰을 받으면 토큰을 추출하여 올바른 토큰인지 체크
	//토큰이 올바르다면, 토큰에 있는 정보(username, role 등등) 을 가져와 인증을 시킨다.
	//토큰이 없거나, 올바르지않다면 그냥 다음으로 넘어간다.

	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private LoginUserDetailsService loginUserDetailsService;


	@Override       //요청당 한번의 filter를 수행하도록
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		/*
		로직에서는 헤더에서 Authorization값을 꺼내어 토큰을 검사하고 해당 유저가 실제 DB에 있는지 검사하는 등의
		전반적인 인증처리를 여기서 진행한다.
		 */

		try {
			String token = getToken(request);

			// 유효한 토큰인지 확인합니다.
			if (token != null && jwtTokenProvider.validateToken(token)) {
				String username = jwtTokenProvider.getUserPk(token);
				
				UserDetails userDetails = loginUserDetailsService.loadUserByUsername(username);     //로그인 객체
				UsernamePasswordAuthenticationToken authentication
						= new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// SecurityContext 에 Authentication 객체를 저장합니다.
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}

		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}

		filterChain.doFilter(request, response);
	}


	private String getToken(HttpServletRequest request) {

		String headerAuth = request.getHeader("Authorization");     //헤더에서 JWT

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}
		return null;
	}

}
