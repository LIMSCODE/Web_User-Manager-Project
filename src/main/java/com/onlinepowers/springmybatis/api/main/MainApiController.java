package com.onlinepowers.springmybatis.api.main;

import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainApiController {

	private final UserService userService;

	@GetMapping("/hello")
	public String welcome() {
		return "/hello";
	}


	/**
	 * 유저 메인페이지 - 메인 API컨트롤러로 옮김
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView userMain(HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		log.debug("메인");

		ModelAndView mv = new ModelAndView();

		if (loginUser == null) {
			mv.setViewName("/main/user");
			return mv;
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();
			mv.setViewName("/main/user");
			return mv;
		}

		if (UserUtils.isUserLogin(session)) {
			mv.addObject("loginUser", loginUser);
			mv.setViewName("/main/user");
			return mv;
		}

		mv.setViewName("/main/user");
		return mv;
	}

	/**
	 * 유저 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/user/logout")
	public ModelAndView userLogout(HttpSession session) {
		session.invalidate();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/");
		return mv;
	}


	/**
	 * 관리자 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/opmanager/logout")
	public ModelAndView managerLogout(HttpSession session) {
		session.invalidate();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/");
		return mv;
	}

}