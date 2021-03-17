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
		int url = request.getRequestURI().indexOf("/user");  //포함하고있을시

		// 세션이 있으면
		if(session != null){
			User loginUser = (User) session.getAttribute("loginUser");

			//로그인한 상태이면 true (보여짐)
			if (loginUser != null) {
				String authority = loginUser.getUserRole().getAuthority();

				// 관리자이면 user포함된 url 접근못하게 ?? 안됨 - MVCconfig에 adminRoleInterceptor추가해줘야함
				if("1".equals(authority)){

					// 사용자이면서 opmanager이 포함되있는 링크로 가면 튕겨냄
				} else {
					if (opUrl > -1) {
						return false;
					}
					return true;
				}

				return true;

				//로그인 안한상태이면 false (안보여짐) - opmanager은 보여져야함.
			} else {

				if (request.getRequestURI().equals("/opmanager")) {
					return true;
				} else {
					return false;
				}

			}
		} else {

			return false;

			// loginUser.getUserRole().getAuthority()
		}

	}


}