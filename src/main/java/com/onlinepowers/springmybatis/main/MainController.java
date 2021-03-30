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

	//유저일때 메인
	@GetMapping("/")
	public String userMain(HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		log.debug("메인");

		//뷰에서 null이 아니면 로그인버튼을 정보수정, 로그아웃버튼으로 변경
		if (loginUser != null) {
			log.debug("1");
			model.addAttribute("loginUser", loginUser);
		}

		return "/main/user";
	}

	@GetMapping("/user/logout")
	public String userLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/opmanager/logout")
	public String managerLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

}