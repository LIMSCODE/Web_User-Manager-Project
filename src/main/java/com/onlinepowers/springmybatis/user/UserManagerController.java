package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequestMapping("/api/opmanager/user")
@Controller
@RequiredArgsConstructor
public class UserManagerController {

	private final UserService userService;


	/**
	 * 관리자 로그인
	 * @param user
	 * @return
	 */
	@GetMapping("/login")
	public String login(User user) {

		return "/opmanager/user/login";
	}


	/**
	 * 회원 목록
	 * @param jpaPaging
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String getUserList(@ModelAttribute("jpaPaging") JpaPaging jpaPaging,
	                          User user, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 2) Pageable pageable,
	                          HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		Page<User> userPage = userService.getUserList(user, pageable, jpaPaging); //페이지 객체 담아서 뷰로 보낸다.
		model.addAttribute("userPage", userPage);

		return "/opmanager/user/list";
	}


	/**
	 * 회원 등록
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public String registerForm(User user, HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/form";
	}


	/**
	 * 회원정보 수정
	 * @param id
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String updateForm(@PathVariable("id") long id, @ModelAttribute("jpaPaging")JpaPaging jpaPaging,
	                         Optional<User> user, HttpSession session, Model model) {

		user = Optional.ofNullable(userService.getUserById(id));
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		//세션 저장 정보
		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/form";
	}


}