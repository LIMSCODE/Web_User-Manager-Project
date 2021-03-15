package com.onlinepowers.springmybatis.main;

import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserDetail;
import com.onlinepowers.springmybatis.user.UserRole;
import com.onlinepowers.springmybatis.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class MainController {
	@Autowired
	private UserService userService;

	//유저일때 메인
	@GetMapping("/")
	public String userMain(HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		log.debug("메인");
		//뷰에서 null이 아니면 로그인버튼을 정보수정, 로그아웃버튼으로 변경
		if (loginUser != null) {
			log.debug("1");
			model.addAttribute("loginUser", loginUser);
		}

		return "/main/user";
	}

	//매니저일때 메인
	@GetMapping("/opmanager")
	public String managerMain(User user, HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		//로그인여부 체크
		if (loginUser == null) {
			return "/opmanager/user/login";
		}
		
		log.debug("메인창으로");

		return "/main/opmanager";
	}

	@GetMapping("/main/opmanager")
	public String managerMain(HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		//뷰에서 null이 아니면 로그인버튼을 정보수정, 로그아웃버튼으로 변경
		if (loginUser != null) {
			model.addAttribute("loginUser", loginUser);
		}

		return "/main/opmanager";
	}
	
	@GetMapping("/user/logout")
	public String userLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	@GetMapping("/opmanager/logout")
	public String managerLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/opmanager";
	}

	//유저일때 메인
	@GetMapping("/regist-form")
	public String register(User user) {

		return "/user/regist-form";
	}

	//회원가입
	@PostMapping("/register")
	public String register(HttpSession session, User user, UserDetail userDetail, UserRole userRole, Model model) {

		user.setUserDetail(userDetail);
		user.setUserRole(userRole); //th:object = user , name이 authority인 태그 받음

		userService.insertUser(user);

		User loginUser = userService.getUserByLoginId(user.getLoginId());
		session.setAttribute("loginUser", loginUser);

		//관리자일때
		if ("1".equals(loginUser.getUserRole())) {

			log.debug("회원가입");
			return "redirect:/opmanager";
			
		} else {
			log.debug("회원가입-사용자");
			return "redirect:/";
		}
	}

	@ResponseBody
	@PostMapping(value = "/check-id")
	public int checkId(User user) throws Exception {

		String loginId = user.getLoginId();
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류

		return userCount > 0 ? 1 : 0 ;
	}
}