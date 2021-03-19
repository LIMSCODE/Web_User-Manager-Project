package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class UserRoleIntercepter implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		log.debug("START");

		HttpSession session = request.getSession(false);
		int opUrl = request.getRequestURI().indexOf("/opmanager");  //포함하고있을시

		// 세션이 있으면
		if (session != null) {
			User loginUser = (User) session.getAttribute("loginUser");

			if (loginUser != null) {
				String authority = loginUser.getUserRole().getAuthority();

				int editUrl = request.getRequestURI().indexOf("/user/edit/" + loginUser.getId());  //포함하고있을시
				int postEditUrl = request.getRequestURI().indexOf("/user/edit/$%7Bid%7D" );  //post로 수정시 뜨는 URL

				if ("ROLE_OPMANAGER".equals(authority)) {
					if (opUrl > -1) {
						return true;
					}
					return false;

				} else {
					// 사용자일때 관리자기능 차단
					if (opUrl > -1) {
						return false;
					}
					// 사용자일때 자기자신것만 수정창 뜨도록
					if (editUrl > -1) {
						return true;
					}
					if (postEditUrl > -1) {
						return true;
					}
					return false;   //원래 MVC에서 설정한 url값에대해 기본값 false
				}

			} else {
				//로그인 안했을때
				if (request.getRequestURI().equals("/opmanager")) {
					return true;
				}
				return true;

			}
		} else {
			//세션자체가 없을때 -로그아웃
			return true;
		}

	}


}