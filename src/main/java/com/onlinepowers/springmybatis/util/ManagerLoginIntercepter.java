package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class ManagerLoginIntercepter implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		log.debug("로그인 인터셉터");
		HttpSession session = request.getSession(false);
		User loginUser = (User) session.getAttribute("loginUser");


		if (loginUser == null) {

			response.sendRedirect("/opmanager/user/login");
			return false;
		}

		return true;

	}


}