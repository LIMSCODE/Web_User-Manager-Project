package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import javax.servlet.http.HttpSession;

public class UserUtils {

	private static String LOGIN_USER = "loginUser";

	/**
	 * 로그인 세션 가져오기
	 * @param session
	 * @return
	 */
	public static User getLoginUser(HttpSession session) {

		if (session == null || session.getAttribute(LOGIN_USER) == null) {     //세션에 저장된게 없을때
			return null;      //혹은 new User() - "/" 컨트롤러에서 로그인된 상태로 인식
		}

		return (User) session.getAttribute(LOGIN_USER);
	}

	/**
	 * 유저인지 확인
	 */
	public static boolean isUserLogin(HttpSession session) {

		User loginUser = getLoginUser(session);    //위의 로그인세션 가져옴

		if (loginUser.getId() == null || loginUser.getUserRole().getAuthority() == null) {    // 로그인유저가
			return false;
		}

		if ("ROLE_USER".equals(loginUser.getUserRole().getAuthority())) {
			return true;
		}

		return false;
	}

	/**
	 * 관리자인지 확인
	 */
	public static boolean isManagerLogin(HttpSession session) {

		User loginUser = getLoginUser (session);  //위의 로그인세션 가져옴

		if (loginUser.getId() == null || loginUser.getUserRole().getAuthority() == null) {
			return false;
		}

		if ("ROLE_OPMANAGER".equals(loginUser.getUserRole().getAuthority())) {
			return true;
		}

		return false;
	}


}
