package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.api.ApiResponseEntity;
import com.onlinepowers.springmybatis.user.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
@RequestMapping("/user")
public class UserApiController {

	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping
	public ResponseEntity<Map<String, Object>> list(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", "123");     //json값으로 출력

		return new ResponseEntity<>(map, HttpStatus.OK);
	}


	@GetMapping("/login")
	public ModelAndView login(User user) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/login");
		return mv;
	}


	@PostMapping("/login")
	public ResponseEntity<String> login(User user, HttpSession session, Model model) {

		// 아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPassword());
		// AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리
		Authentication authentication = authenticationManager.authenticate(token);
		// 실제 SecurityContext 에 authentication 정보를 등록
		SecurityContextHolder.getContext().setAuthentication(authentication);


		ResponseEntity<String> responseEntity = null;
		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		log.debug("-----------로그인 컨트롤러");
		
		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		String storedPassword = loginUser.getPassword();    //항상 일정
		log.debug(storedPassword);

		if (!passwordEncoder.matches(user.getPassword(), storedPassword)) {
			log.debug("비밀번호 일치하지않음");
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		if (!"ROLE_USER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);
		model.addAttribute("loginUser", loginUser);


		return responseEntity;
	}


	/**
	 * 회원가입
	 * @param user
	 * @return
	 */
	@GetMapping("/create")
	public ModelAndView register(User user) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/form");
		return mv;
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
			//model.addAttribute("user", user);
			responseEntity = new ResponseEntity("register_fail", HttpStatus.BAD_REQUEST);
			return responseEntity;
			// return "/user/form";
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");
			responseEntity = new ResponseEntity("register_fail", HttpStatus.BAD_REQUEST);
			return responseEntity;
			//return "redirect:/user/create";
		}

		userService.insertUser(user);

		User loginUser = userService.getUserByLoginId(user.getLoginId());
		session.setAttribute("loginUser", loginUser);

		responseEntity = new ResponseEntity("register_success", HttpStatus.OK);
		return responseEntity;

	}


	/**
	 * 수정 전 비밀번호 확인
	 * @param user
	 * @return
	 */
	@GetMapping("/password-check")
	public ModelAndView checkPassword(User user) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/user/password-check");
		return mv;
	}


	/**
	 * 수정 전 비밀번호 확인
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/password-check")
	public ResponseEntity<Map<String, Object>> checkPassword(User user, HttpSession session, Model model) {

		ResponseEntity<Map<String, Object>> responseEntity = null;
		Map<String, Object> map = new HashMap<String, Object>();
		User loginUser = UserUtils.getLoginUser(session);

		//입력받은 비밀번호
		log.debug(loginUser.getPassword());
		log.debug(passwordEncoder.encode(user.getPassword()));

		//로그인 세션의 비밀번호 값과 일치하는지 확인
		if (!loginUser.getPassword().equals(passwordEncoder.encode(user.getPassword()))) {
			log.debug("비밀번호 일치하지 않음 컨트롤러");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		log.debug("비밀번호 일치, 수정폼으로 이동 ");
		map.put("id", loginUser.getId());
		return new ResponseEntity<>(map, HttpStatus.OK);
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
	public ModelAndView updateForm(@PathVariable("id") long id,
							 Optional<User> user, HttpSession session, Model model) {

		ModelAndView mv = new ModelAndView();
		user = Optional.ofNullable(userService.getUserById(id));

		mv.addObject("user", user);
		mv.addObject("id", user.get().getId());
		mv.setViewName("/user/form");
		return mv;
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
			responseEntity = new ResponseEntity("다시입력해주세요", HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		userService.updateUser(user);

		User updatedUser = userService.getUserByLoginId(user.getLoginId());     //비밀번호 수정후 바뀐 DTO를 session에 set해줘야함.
		session.setAttribute("loginUser", updatedUser);

		responseEntity = new ResponseEntity("MOD_SUCCEEDED",HttpStatus.OK);
		return responseEntity;
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




