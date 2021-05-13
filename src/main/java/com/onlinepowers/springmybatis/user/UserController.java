package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;


	/**
	 * 유저 로그인
	 * @param user
	 * @return
	*/
	@GetMapping("/login")
	public String login(User user) {

		return "/user/login";
	}


	/**
	 * 회원가입
	 * @param user
	 * @return
	 */
	@GetMapping("/create")
	public String register(User user) {

		return "/user/form";
	}


	/**
	 * 수정 전 비밀번호 확인
	 * @param user
	 * @return
	 */
	@GetMapping("/password-check")
	public String checkPassword(User user, @AuthenticationPrincipal LoginUserDetails securityUser) {
		log.debug("비밀번호 확인 창=============" + securityUser.getUser().getUserRole());

		return "/user/password-check";
	}


	/**
	 * 개인정보 수정
	 * @param id
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String updateForm(@PathVariable("id") long id,
	                         Optional<User> user, HttpSession session, Model model, @AuthenticationPrincipal LoginUserDetails securityUser) {
		
		log.debug("수정창=============" + securityUser.getUser().getUserRole());

		//api ajax로 정보 불러옴
		//user = Optional.ofNullable(userService.getUserById(id));
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", user.get().getId());   //form 뷰에서 id있을때로 처리됨.

		return "/user/form";
	}


}




