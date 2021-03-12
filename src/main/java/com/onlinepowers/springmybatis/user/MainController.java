package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.util.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class MainController {
	@Autowired
	private UserService userService;

	@GetMapping("/main")
	public String main() {

		return "/main";
	}

	@GetMapping("/login")
	public String login(User user) {

		return "/login";
	}

	/**
	 * 로그인 버튼 누름
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public String login(User user, HttpSession session) {

		//입력아이디에 해당하는 유저가 DB에 있으면
		if (userService.getUserByLoginId(user.getLoginId()) != null) {

			//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
			User loginUser = userService.getUserByLoginId(user.getLoginId());

			//입력한 비밀번호를 해시함수로
			String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

			log.debug(hashPassword);
			log.debug(loginUser.getPassword());

			//DB에 저장된 비밀번호 값과 일치하는지 확인
			if (loginUser.getPassword().equals(hashPassword)) {

				//비밀번호 일치시 세션에 담는다.
				session.setAttribute("loginUser", loginUser);
				session.setMaxInactiveInterval(1000 * 1000);

				log.debug("비밀번호 일치");

				//관리자일때
				if ("1".equals(loginUser.getUserRole().getAuthority())) {

					return "redirect:/opmanager/user/list";

				//사용자일때
				} else {

					return "redirect:/user/after-login";
				}
			} else {
				log.debug("비밀번호 일치하지 않음");
				return "redirect:/user/login";
			}

		} else {
			log.debug("해당하는 아이디가 없음");
			return "redirect:/user/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();  // 세션 초기화
		return "redirect:/main";
	}

}