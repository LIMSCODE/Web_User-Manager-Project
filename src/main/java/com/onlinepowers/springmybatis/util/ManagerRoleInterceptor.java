package com.onlinepowers.springmybatis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class ManagerRoleInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		HttpSession session = request.getSession(false);

		if (UserUtils.getLoginUser(session) == null) {
			return true;
		}

		boolean isManagerUrl = request.getRequestURI().startsWith("/opmanager");

		if (UserUtils.isManagerLogin(session) && !isManagerUrl) {  // 관리자 로그인시, opmanager을 포함하지 않으면 모두 막는다.
			return false;
		}

		return true;   //원래 MVC에서 설정한 url값에대해 원래는 접근가능

	}

}