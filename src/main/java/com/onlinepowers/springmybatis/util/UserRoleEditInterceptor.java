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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {


//		int editUrl = request.getRequestURI().indexOf("/user/edit/" + loginUser.getId());  //포함하고있을시
//
//		if (editUrl > -1) {		// 사용자일때 자기자신것만 수정창 뜨도록
//
//			return true;
//		}

		String requestURI = request.getRequestURI();
		int idx = requestURI.lastIndexOf('/') +1;
		long id = Integer.parseInt(requestURI.substring(idx));

		HttpSession session = request.getSession(false);
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser.getId() == id) { 		//본인페이지는 수정가능
			
			return true;
		}
		//출처: https://sourcestudy.tistory.com/372 [study]

		return false;	//원래 MVC에서 설정한 url값에대해 기본값 false
	}

}