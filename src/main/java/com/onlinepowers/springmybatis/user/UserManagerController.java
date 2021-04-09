package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/opmanager/user")
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
	 * 관리자 로그인
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");
			return "redirect:/opmanager/user/login";
		}

		//입력한 비밀번호를 해시함수로
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//비밀번호 일치하지않으면
		if (!loginUser.getPassword().equals(hashPassword)) {
			log.debug("비밀번호 일치하지 않음");
			return "redirect:/opmanager/user/login";
		}

		//관리자가 아닐때
		if (!"ROLE_OPMANAGER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			return "redirect:/user/login";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		return "redirect:/opmanager";
	}


	/**
	 * 회원 목록
	 * @param cri
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/list")
	public String getUserList(@ModelAttribute("cri") Criteria cri,
	                          User user,
	                          HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		List<User> userList = userService.getUserList(user, cri);
		model.addAttribute("userList", userList);

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
	 * 회원 등록
	 * @param user
	 * @param userResult
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/create")
	public String createUser(@Valid User user, BindingResult userResult,
							 HttpSession session, Model model) {

		if (userResult.hasErrors()) {
			User loginUser = UserUtils.getLoginUser(session);

			model.addAttribute("loginUser", loginUser);
			model.addAttribute("user", user);
			return "/opmanager/user/form";
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");
			return "redirect:/opmanager/user/create";
		}

		userService.insertUser(user);

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		//관리자페이지에서 등록하는 경우
		return "redirect:/opmanager/user/list";
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
	public String updateForm(@PathVariable("id") long id, @ModelAttribute("cri") Criteria cri,
	                         Optional<User> user, HttpSession session, Model model) {

		user = userService.getUserById(id);
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		//세션 저장 정보
		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/form";
	}


	/**
	 * 회원정보 수정
	 * @param id
	 * @param cri
	 * @param user
	 * @param userResult
	 * @param session
	 * @param model
	 * @param rttr
	 * @return
	 */
	@PostMapping("/edit/{id}")
	public String updateUser(@PathVariable("id") long id, @ModelAttribute("cri") Criteria cri,
							 @Valid User user,  BindingResult userResult,
							 HttpSession session, Model model, RedirectAttributes rttr) {

		if (userResult.hasErrors()) {
			User loginUser = UserUtils.getLoginUser(session);

			model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.
			model.addAttribute("loginUser", loginUser);
			model.addAttribute("user", user);
			return "/opmanager/user/form";
		}

		user.getUserDetail().setUserId(user.getId());
		user.getUserRole().setUserId(user.getId());

		userService.updateUser(user);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/opmanager/user/list";

	}


	/**
	 * 회원 삭제
	 * @param id
	 * @param cri
	 * @param rttr
	 * @return
	 */
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, @ModelAttribute("cri") Criteria cri,
	                         RedirectAttributes rttr) {

		userService.deleteUserById(id);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/opmanager/user/list";
	}


	/**
	 * 회원 등록시 아이디 중복 확인
	 * @param user
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/check-id")
	public int checkId(User user) {

		String loginId = user.getLoginId();
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류
		log.debug(String.valueOf(userCount));

		return userCount > 0 ? 1 : 0 ;
	}

}