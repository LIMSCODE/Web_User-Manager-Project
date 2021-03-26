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

		if (session == null) {		//로그아웃시
			return true;
		}

		if (UserUtils.getLoginUser(session) == null) {
			return true;
		}

		int isManagerPage = request.getRequestURI().indexOf("/opmanager");
		if (UserUtils.isManagerLogin(session) && isManagerPage == -1) {  // 관리자 로그인시, opmanager을 포함하지 않으면 모두 막는다.
			return false;
		}

		return true;   //원래 MVC에서 설정한 url값에대해 원래는 접근가능

	}

}