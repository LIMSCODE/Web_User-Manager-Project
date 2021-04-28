package com.onlinepowers.springmybatis.api.main;

import com.onlinepowers.springmybatis.user.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainApiController {

	@GetMapping("/hello")
	public ModelAndView welcome(Principal user) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/main/user");

		System.out.println("================= " + user);
		return mv;
	}


	/**
	 * 유저 메인페이지 - 메인 API컨트롤러로 옮김
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView userMain(HttpSession session, Model model, @AuthenticationPrincipal LoginUserDetails user) {

		User loginUser = UserUtils.getLoginUser(session);

		log.debug("메인");

		ModelAndView mv = new ModelAndView();

		if (loginUser == null) {
			mv.setViewName("/main/user");
			System.out.println("=================널 " + user);
			return mv;
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();
			mv.setViewName("/main/user");
			System.out.println("=================매니저 " + user);
			return mv;
		}

		if (UserUtils.isUserLogin(session)) {       //시큐리티 적용후 유저 로그인, 여기까지 넘어옴
			
			mv.addObject("loginUser", user);
			mv.setViewName("/main/user");

			System.out.println("================= " + user);
			System.out.println("================= " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
				//anonymousUser
			return mv;
		}

		mv.setViewName("/main/user");
		System.out.println("=================막 " + user);
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