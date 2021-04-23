package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.api.ApiResponseEntity;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

	private final UserService userService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> list(HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", "123");     //json값으로 출력
		return new ResponseEntity<>(map, HttpStatus.OK);
	}


	/**
	 * 유저 메인페이지 - 메인 API컨트롤러로 옮김
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView userMain(HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		log.debug("메인");

		ModelAndView mv = new ModelAndView();

		if (loginUser == null) {

			mv.setViewName("/api/main/user");
			return mv;
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();

			mv.setViewName("/api/main/user");
			return mv;
		}

		if (UserUtils.isUserLogin(session)) {
								   //null이 아니면 정보수정 뜨고, null이면 회원가입 뜬다.

			mv.addObject("loginUser", loginUser);
			mv.setViewName("/api/main/user");
			return mv;
		}

		mv.setViewName("/api/main/user");
		return mv;
	}


	@GetMapping("/login")
	public ModelAndView login(User user) {

		ModelAndView mv = new ModelAndView();

		mv.setViewName("/api/main/login");
		return mv;
	}

	@PostMapping("/login1")
	public ResponseEntity<String> login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");

			return new ResponseEntity<String>("로그인실패", HttpStatus.OK);
			//return "redirect:/user/login";
		}

		//입력한 비밀번호를 해시함수로
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//비밀번호 일치하지않으면
		if (!loginUser.getPassword().equals(hashPassword)) {
			log.debug("비밀번호 일치하지 않음");

			return new ResponseEntity<String>("로그인실패", HttpStatus.OK);
			//return "redirect:/user/login";
		}

		//사용자가 아닐때
		if (!"ROLE_USER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");

			return new ResponseEntity<String>("로그인실패", HttpStatus.OK);
			//return "redirect:/user/login";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		model.addAttribute("loginUser", loginUser);

		return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
		//return "redirect:/";
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
	public ResponseEntity<String> register(@Valid User user, BindingResult userResult,
	                       HttpSession session, Model model) {

		ResponseEntity<String> responseEntity = null;

		if (userResult.hasErrors()) {
			model.addAttribute("user", user);

			return responseEntity;
			// return "/user/form";
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");

			return responseEntity;
			//return "redirect:/user/create";
		}

		userService.insertUser(user);

		User loginUser = userService.getUserByLoginId(user.getLoginId());

		session.setAttribute("loginUser", loginUser);
		model.addAttribute("loginUser", loginUser);

		return responseEntity;
		//return "redirect:/";
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
	public ResponseEntity<String> updateUser(@PathVariable("id") long id,
	                         @Valid User user, BindingResult userResult,
	                         HttpSession session, Model model) {

		ResponseEntity<String> responseEntity = null;

		if (userResult.hasErrors()) {
			model.addAttribute("id", user.getId());
			model.addAttribute("user", user);

			return responseEntity;
			//return "/user/form";
		}

		userService.updateUser(user);

		User updatedUser = userService.getUserByLoginId(user.getLoginId());     //비밀번호 수정후 바뀐 DTO를 session에 set해줘야함.
		session.setAttribute("loginUser", updatedUser);

		return responseEntity;
		//return "redirect:/";
	}


	/**
	 * 아이디 중복 확인
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/check-id")
	public ResponseEntity<Map<String, Object>> checkId(User user) {

		String loginId = user.getLoginId();
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류
		log.debug(String.valueOf(userCount));

		Map<String, Object> map = new HashMap<String, Object>();

		if (userCount > 0) {
			map.put("isDuplicated", false);

		} else {
			map.put("isDuplicated", true);

		}
		return new ResponseEntity<>(map, HttpStatus.OK);
	}


}




