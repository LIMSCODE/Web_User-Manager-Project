package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
	 * 유저 로그인
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
		if (!"ROLE_USER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			return "redirect:/user/login";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		model.addAttribute("loginUser", loginUser);

		return "redirect:/";
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
	 * 회원가입
	 * @param user
	 * @param userResult
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/create")
	public String register(@Valid User user, BindingResult userResult,
	                       HttpSession session, Model model) {

		if (userResult.hasErrors()) {
			model.addAttribute("user", user);
			return "/user/form";
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");
			return "redirect:/user/create";
		}

		userService.insertUser(user);

		User loginUser = userService.getUserByLoginId(user.getLoginId());

		session.setAttribute("loginUser", loginUser);
		model.addAttribute("loginUser", loginUser);

		return "redirect:/";
	}


	/**
	 * 수정 전 비밀번호 확인
	 * @param user
	 * @return
	 */
	@GetMapping("/password-check")
	public String checkPassword(User user) {

		return "/user/password-check";
	}


	/**
	 * 수정 전 비밀번호 확인
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/password-check")
	public String checkPassword(User user, HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);

		//입력받은 비밀번호
		log.debug(user.getPassword());
		//입력한 비밀번호를 PK이용하여 해시함수로 만들고
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//로그인 세션의 비밀번호 값과 일치하는지 확인
		if (!hashPassword.equals(loginUser.getPassword())) {
			log.debug("비밀번호 일치하지 않음 컨트롤러");
			return "redirect:/user/password-check";
		}

		log.debug("비밀번호 일치, 수정폼으로 이동 ");
		return "redirect:/user/edit/" + loginUser.getId();
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
	                         Optional<User> user, HttpSession session, Model model) {

		user = Optional.ofNullable(userService.getUserById(id));

		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", user.get().getId());   //form 뷰에서 id있을때로 처리됨.

		return "/user/form";
	}


	/**
	 * 개인정보 수정
	 * @param id
	 * @param user
	 * @param userResult
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/edit/{id}")
	public String updateUser(@PathVariable("id") long id,
	                         @Valid User user, BindingResult userResult,
	                         HttpSession session, Model model) {

		if (userResult.hasErrors()) {
			model.addAttribute("id", user.getId());
			model.addAttribute("user", user);
			return "/user/form";
		}

		userService.updateUser(user);

		User updatedUser = userService.getUserByLoginId(user.getLoginId());     //비밀번호 수정후 바뀐 DTO를 session에 set해줘야함.
		session.setAttribute("loginUser", updatedUser);

		return "redirect:/";
	}


	/**
	 * 아이디 중복 확인
	 * @param user
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/check-id")
	public Map<String, Object> checkId(User user) {
		String loginId = user.getLoginId();
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류
		log.debug(String.valueOf(userCount));

		Map<String, Object> map=new HashMap<String, Object>();

		if (userCount > 0) {
			map.put("isDuplicated", false);

		} else {
			map.put("isDuplicated", true);

		}
		return map;

	}

}




