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

	//사용자일때 로그인
	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");
			return "redirect:/user/login";
		}

		//입력한 비밀번호를 해시함수로
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//비밀번호 일치하지않으면
		if (!loginUser.getPassword().equals(hashPassword)) {
			log.debug("비밀번호 일치하지 않음");
			return "redirect:/user/login";
		}
		//사용자가 아닐때
		if ("ROLE_OPMANAGER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			return "redirect:/user/login";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		model.addAttribute("loginUser", loginUser);

		return "redirect:/";
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

			log.debug("비밀번호 일치, 수정폼으로 이동 ");

			String authority = loginUser.getUserRole().authority;
			log.debug(authority);  //0
			model.addAttribute("authority", authority);

			return "redirect:/user/edit/" + loginUser.getId();

		} else {

			log.debug("비밀번호 일치하지 않음 컨트롤러");

			return "redirect:/user/password-check";
		}
	}

	@GetMapping("/edit/{id}")
	public String updateForm(@PathVariable("id") long id,
	                         @ModelAttribute("cri") Criteria cri,
	                         User user, HttpSession session,  Model model) {

		user = userService.getUserById(id);
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", user.getId());   //form 뷰에서 id있을때로 처리됨.

		//세션 저장 정보
		User loginUser = (User) session.getAttribute("loginUser");

		//관리자일때만 목록 링크, 권한 수정 보이도록한다.
		String authority = loginUser.getUserRole().getAuthority();
		if ("ROLE_OPMANAGER".equals(authority)) {
			model.addAttribute("authority", authority);   //form 뷰에서 id있을때로 처리됨.
		}

		return "/user/form";
	}

	@PostMapping("/edit/{id}")
	public String updateUser(
							 @ModelAttribute("cri") Criteria cri,
	                         User user, UserDetail userDetail, UserRole userRole,
	                         HttpSession session, Model model, RedirectAttributes rttr) {

		//user.getLoginId 로 입력받은값이 loginUser.
		User loginUser = (User) session.getAttribute("loginUser");

		//하위 테이블 수정안되는 현상 해결
		userDetail.setUserId(user.getId());
		userRole.setUserId(user.getId());

		user.setUserDetail(userDetail);
		user.setUserRole(userRole);

		userService.updateUser(user);

		return "redirect:/";

		}
}




