package com.onlinepowers.springmybatis.main;

import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

	private final UserService userService;

	@GetMapping("/hello")
	public String welcome() {
		return "/hello";
	}


	/**
	 * 유저 메인페이지
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String userMain(HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		log.debug("메인");

		if (loginUser == null) {
			return "/main/user";
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();
			return "/main/user";
		}

		if (UserUtils.isUserLogin(session)) {
			model.addAttribute("loginUser", loginUser);
			return "/main/user";    //null이 아니면 정보수정 뜨고, null이면 회원가입 뜬다.
		}

		return "/main/user";
	}


	/**
	 * 유저 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/user/logout")
	public String userLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}


	/**
	 * 관리자 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/opmanager/logout")
	public String managerLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}