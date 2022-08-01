package com.onlinepowers.springmybatis.main;

import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {


	/**
	 * 유저 메인페이지
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public String userMain(HttpSession session, Model model, @AuthenticationPrincipal LoginUserDetails securityUser) {

		User loginUser = UserUtils.getLoginUser(session);

		if (loginUser == null) {
			log.debug("메인=============" );

			return "/main/user";
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();
			return "/main/user";
		}

		if (UserUtils.isUserLogin(session)) {
			//log.debug("메인=============" + securityUser.getUser().getUserRole());

			model.addAttribute("loginUser", loginUser);
			return "/main/user";    //null이 아니면 정보수정 뜨고, null이면 회원가입 뜬다.
		}

		return "/main/user";
	}


}
