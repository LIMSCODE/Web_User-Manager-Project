package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.jwt.JwtTokenProvider;
import com.onlinepowers.springmybatis.paging.JpaPaging;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/opmanager/user")
public class UserManagerApiController {

	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	/**
	 * 관리자 로그인
	 * @param user
	 * @return
	 */
	@GetMapping("/login")
	public ModelAndView login(User user) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/opmanager/user/login");
		return mv;
	}


	/**
	 * 관리자 로그인
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<String> login(User user, HttpSession session, Model model) {
	
		//이거 세줄 써야 @Authen 메인컨트롤러에서 받와와짐, 안쓰면 null임
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
			//return responseEntity;
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.OK);
			return responseEntity;
		}

		String storedPassword = loginUser.getPassword();    //항상 일정
		log.debug(storedPassword);

		//비밀번호 일치하지않으면
		if (!passwordEncoder.matches(user.getPassword(), storedPassword)) {
			log.debug("비밀번호 일치하지 않음");
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.OK);
			return responseEntity;
		}

		//관리자가 아닐때
		if (!"ROLE_OPMANAGER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			responseEntity = new ResponseEntity("Login_fail",HttpStatus.OK);
			return responseEntity;
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		responseEntity = new ResponseEntity("Login_SUCCEEDED",HttpStatus.OK);
		return responseEntity;
	}


	@GetMapping("/list")
	public ModelAndView getUserList1(@ModelAttribute("jpaPaging") JpaPaging jpaPaging,
	                                 User user, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 2) Pageable pageable,
	                                 HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		Page<User> userPage = userService.getUserList(user, pageable, jpaPaging); //페이지 객체 담아서 뷰로 보낸다.
		//model.addAttribute("userPage", userPage);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/opmanager/user/list");
		mv.addObject("userPage", userPage);

		return mv;
	}


	/**
	 * 회원 목록
	 * @param jpaPaging
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/list1")
	public ResponseEntity<Page<User>> getUserList(@ModelAttribute("jpaPaging") JpaPaging jpaPaging,
	                          User user, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC, size = 2) Pageable pageable,
	                          HttpSession session, Model model) {

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		Page<User> userPage = userService.getUserList(user, pageable, jpaPaging); //페이지 객체 담아서 뷰로 보낸다.
		model.addAttribute("userPage", userPage);

		return new ResponseEntity<>(userPage, HttpStatus.OK);
	}


	/**
	 * 회원 등록
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/create")
	public ModelAndView registerForm(User user, HttpSession session, Model model) {

		ModelAndView mv = new ModelAndView();
		User loginUser = UserUtils.getLoginUser(session);

		mv.addObject("loginUser", loginUser);
		mv.setViewName("/opmanager/user/form");
		return mv;
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
	public ResponseEntity<String> createUser(@Valid User user, BindingResult userResult,
	                         HttpSession session, Model model) {

		ResponseEntity<String> responseEntity = null;

		if (userResult.hasErrors()) {
			User loginUser = UserUtils.getLoginUser(session);

			model.addAttribute("loginUser", loginUser);
			model.addAttribute("user", user);

			responseEntity = new ResponseEntity("다시입력해주세요", HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");

			responseEntity = new ResponseEntity("해당아이디 존재", HttpStatus.BAD_REQUEST);

			return responseEntity;
			//return "redirect:/opmanager/user/create";
		}

		userService.insertUser(user);

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		responseEntity = new ResponseEntity("ADD_SUCCEEDED", HttpStatus.OK);
		return responseEntity;

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
	public ModelAndView updateForm(@PathVariable("id") long id, @ModelAttribute("jpaPaging")JpaPaging jpaPaging,
							 Optional<User> user, HttpSession session, Model model) {

		ModelAndView mv = new ModelAndView();

		user = Optional.ofNullable(userService.getUserById(id));
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		//세션 저장 정보
		User loginUser = UserUtils.getLoginUser(session);

		mv.addObject("loginUser", loginUser);
		mv.setViewName("/opmanager/user/form");
		return mv;
	}


	/**
	 * 회원정보 수정
	 * @param id
	 * @param jpaPaging
	 * @param user
	 * @param userResult
	 * @param session
	 * @param model
	 * @param rttr
	 * @return
	 */
	@PostMapping("/edit/{id}")
	public ResponseEntity<String> updateUser(@PathVariable("id") long id, @ModelAttribute("jpaPaging") JpaPaging jpaPaging,
	                         @Valid User user,  BindingResult userResult,
	                         HttpSession session, Model model, RedirectAttributes rttr) {

		ResponseEntity<String> responseEntity = null;

		if (userResult.hasErrors()) {
			User loginUser = UserUtils.getLoginUser(session);

			model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.
			model.addAttribute("loginUser", loginUser);
			model.addAttribute("user", user);

			responseEntity = new ResponseEntity("다시입력해주세요",HttpStatus.BAD_REQUEST);

			return responseEntity;
			//return "/opmanager/user/form";
		}

		userService.updateUser(user);

		rttr.addAttribute("page", jpaPaging.getPage());
		rttr.addAttribute("searchType", jpaPaging.getSearchType());
		rttr.addAttribute("searchKeyword", jpaPaging.getSearchKeyword());

		responseEntity = new ResponseEntity("MOD_SUCCEEDED",HttpStatus.OK);

		return responseEntity;
	}


	/**
	 * 회원 삭제
	 * @param id
	 * @param jpaPaging
	 * @param rttr
	 * @return
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id, @ModelAttribute("jpaPaging") JpaPaging jpaPaging,
	                         RedirectAttributes rttr) throws URISyntaxException {

		ResponseEntity<String> responseEntity = null;

		userService.deleteUserById(id);

		/*
		rttr.addAttribute("page", jpaPaging.getPage());
		rttr.addAttribute("searchType", jpaPaging.getSearchType());
		rttr.addAttribute("searchKeyword", jpaPaging.getSearchKeyword());
		*/

		responseEntity = new ResponseEntity("Delete Sucess", HttpStatus.OK);
		return responseEntity;

		//return "redirect:/opmanager/user/list";
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




