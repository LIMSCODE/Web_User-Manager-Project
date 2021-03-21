package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class UserRoleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		HttpSession session = request.getSession(false);

		if (session == null) {		//로그아웃시

			return true;
		}

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {	//로그인 안된상태에서는 접근불가

			return false;
		}

		String authority = loginUser.getUserRole().getAuthority();

		if ("ROLE_USER".equals(authority)) {	//유저일떄는 접근불가능

			return false;
		}

		return true;   //원래 MVC에서 설정한 url값에대해 원래는 접근가능

	}

}