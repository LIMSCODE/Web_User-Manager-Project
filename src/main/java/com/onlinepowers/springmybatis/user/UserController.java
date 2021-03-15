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
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(User user) {

		return "/user/login";
	}

	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		if (loginUser != null) {

			//입력한 비밀번호를 해시함수로
			String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
			log.debug(hashPassword);
			log.debug(loginUser.getPassword());

			//비밀번호 일치하지않으면
			if (!loginUser.getPassword().equals(hashPassword)) {
				log.debug("비밀번호 일치하지 않음");
				return "redirect:/user/login";
			}

			//사용자일때
			if ("0".equals(loginUser.getUserRole().getAuthority())) {

				session.setAttribute("loginUser", loginUser);
				session.setMaxInactiveInterval(1000 * 1000);

				model.addAttribute("loginUser", loginUser);
				return "redirect:/";
			}
		}

		log.debug("해당 아이디 없음");
		return "redirect:/user/login";
	}


	//로그인 후 수정하려할때 비밀번호 확인
	@GetMapping("/password-check")
	public String checkPassword() {

		return "/user/password-check";
	}

	@PostMapping("/password-check")
	public String checkPassword(User user, HttpSession session, Model model) {
		
		//세션 저장 정보
		User loginUser = (User) session.getAttribute("loginUser");
		
		//입력받은 비밀번호
		log.debug(user.getPassword());
		//입력한 비밀번호를 PK이용하여 해시함수로 만들고
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//로그인 세션의 비밀번호 값과 일치하는지 확인
		if (hashPassword.equals(loginUser.getPassword())) {

			log.debug("비밀번호 일치, 수정폼으로 이동");
			model.addAttribute("authority", loginUser.getUserRole());

			return "redirect:/opmanager/user/edit/" + loginUser.getId();

		} else {

			log.debug("비밀번호 일치하지 않음 컨트롤러");

			return "redirect:/user/password-check";
		}

	}



}