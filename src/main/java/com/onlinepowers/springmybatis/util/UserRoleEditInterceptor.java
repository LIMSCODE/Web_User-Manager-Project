package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class UserRoleEditInterceptor implements HandlerInterceptor {

	private static String REFERER = "REFERER";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		String requestURI = request.getRequestURI();
		int idx = requestURI.lastIndexOf('/') + 1;        //url 마지막 '/'이후의 값 가져와서 세션의 Id와 비교
		long id = Integer.parseInt(requestURI.substring(idx));

		HttpSession session = request.getSession(false);
		User loginUser = UserUtils.getLoginUser(session);

		String referer = request.getHeader(REFERER);      //직전 페이지에서만 접근허용

		if (referer == null) {
			return false;
		}

		if (loginUser.getId() == id && referer.indexOf("/user/password-check") > -1) { 		//본인페이지만 수정가능
			return true;
		}

		if (loginUser.getId() == id && referer.indexOf("/user/edit/" + id) > -1) { 		//포스트 누를시 직전페이지 허용
			return true;
		}

		return false;	//원래 MVC에서 설정한 url값에대해 기본값 false
	}

}