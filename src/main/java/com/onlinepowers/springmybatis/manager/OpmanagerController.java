package com.onlinepowers.springmybatis.manager;

import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class OpmanagerController {

	/**
	 * 관리자 메인페이지
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/opmanager")
	public String managerMain(User user, HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);

		//로그인안됬거나, 유저일때
		if (loginUser == null || UserUtils.isUserLogin(session)) {
			return "/opmanager/user/login";
		}

		//관리자일때
		model.addAttribute("loginUser", loginUser);

		return "/opmanager/index";
	}

}
