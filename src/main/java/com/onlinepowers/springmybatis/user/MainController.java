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
	public String main(@ModelAttribute("user") User user) {

		return "/main";
	}

	@GetMapping("/login")
	public String login(@ModelAttribute("user") User user) {

		return "/login";
	}

	/**
	 * 로그인 버튼 누름
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public String userLogin(User user, HttpSession session) {

		//만약 입력아이디에 해당하는 유저가 DB에 있으면
		if (userService.getUserByLoginId(user.getLoginId()) != null) {

			//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
			User loginUser = userService.getUserByLoginId(user.getLoginId());

			//입력한 비밀번호를 PK이용하여 해시함수로 만들고
			String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

			log.debug(hashPassword);
			log.debug(loginUser.getPassword());

			//DB에 저장된 비밀번호 값과 일치하는지 확인
			if (hashPassword.equals(loginUser.getPassword())) {

				//비밀번호 일치시 세션에 담는다.
				session.setAttribute("loginUser", loginUser);
				session.setMaxInactiveInterval(1000 * 1000);

				log.debug("비밀번호 일치");

				if (loginUser.getUserRole().getAuthority().equals("1")) {

					return "redirect:/opmanager/user/list";

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

	@GetMapping("/user/after-login")
	public String afterLogin(@ModelAttribute("user") User user, HttpSession session) {

		//redirect를 받는 GetMapping, 세션에 저장된 User객체 받음
		User loginUser = (User) session.getAttribute("loginUser");
		log.debug("로그인 함");
		log.debug(loginUser.getLoginId());

		return "/user/after-login";
	}


	@PostMapping("/opmanager/user/login")
	public String userManagerLogin(User user, HttpSession session, @ModelAttribute("cri") Criteria cri, Model model) {

		//만약 입력아이디에 해당하는 유저가 DB에 있으면
		if (userService.getUserByLoginId(user.getLoginId()) != null) {

			//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
			User loginUser = userService.getUserByLoginId(user.getLoginId());

			//입력한 비밀번호를 PK이용하여 해시함수로 만들고
			String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

			log.debug(hashPassword);
			log.debug(loginUser.getPassword());


			//DB에 저장된 비밀번호 값과 일치하는지 확인
			if (hashPassword.equals(loginUser.getPassword())) {

				//비밀번호 일치시 세션에 담는다.
				session.setAttribute("loginUser", loginUser);
				session.setMaxInactiveInterval(1000 * 1000);

				List<User> userList = userService.getUserList(user, cri);
				model.addAttribute("userList", userList);

				return "redirect:/opmanager/user/list";

			} else {
				log.debug("비밀번호 일치하지 않음");
				return "redirect:/opmanager/user/login";
			}

		} else {
			log.debug("해당하는 아이디가 없음");
			return "redirect:/opmanager/user/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();  // 세션 초기화
		return "redirect:/main";
	}

}