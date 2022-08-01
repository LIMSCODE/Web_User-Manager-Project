package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.jwt.JwtTokenProvider;
import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins="*", maxAge = 3600)
public class ApiUserController {

	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;


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
			responseEntity = new ResponseEntity("register_fail", HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());

		if (storedUser != null) {
			log.debug("해당아이디 존재");
			responseEntity = new ResponseEntity("register_fail", HttpStatus.BAD_REQUEST);
			return responseEntity;
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
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/password-check")
	public ResponseEntity<Map<String, Object>> checkPassword(User user, HttpSession session, Model model
														,@AuthenticationPrincipal LoginUserDetails securityUser) {

		ResponseEntity<Map<String, Object>> responseEntity = null;
		Map<String, Object> map = new HashMap<String, Object>();
		User loginUser = UserUtils.getLoginUser(session);

		String storedPassword = securityUser.getUser().getPassword();	//시큐리티객체에서 가져오는법

		//입력받은 비밀번호
		log.debug(storedPassword);
		log.debug(passwordEncoder.encode(user.getPassword()));

		//로그인 세션의 비밀번호 값과 일치하는지 확인
		if (!passwordEncoder.matches(user.getPassword(), storedPassword)) {
			log.debug("비밀번호 일치하지 않음 컨트롤러");
			return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
		}

		log.debug("비밀번호 일치, 수정폼으로 이동 ");
		map.put("id", securityUser.getUser().getId());
		return new ResponseEntity<>(map, HttpStatus.OK);
	}



	/**
	 * 수정 정보 불러오기
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/edit-detail")
	public ResponseEntity<User> getUserListByAjax(User user, HttpSession session, Model model
			,@AuthenticationPrincipal LoginUserDetails securityUser) {

		User loginUser = securityUser.getUser();
		log.debug(String.valueOf(loginUser));   //null이다.

		return new ResponseEntity<>(loginUser, HttpStatus.OK);
	}


	/**
	 * 개인정보 수정
	 * @param id
	 * @param user
	 * @param userResult
	 * @param session
	 * @return
	 */
	@PutMapping("/edit/{id}")
	public ResponseEntity<String> updateUser(@PathVariable("id") long id,
	                         @Valid User user, BindingResult userResult,
	                         HttpSession session, @AuthenticationPrincipal LoginUserDetails securityUser) {

		ResponseEntity<String> responseEntity = null;

		if (userResult.hasErrors()) {
			responseEntity = new ResponseEntity("다시입력해주세요", HttpStatus.BAD_REQUEST);
			return responseEntity;
		}

		userService.updateUser(user);

		log.debug("수정 컨트롤러 진입");
		User updatedUser = securityUser.getUser();     //비밀번호 수정후 바뀐 DTO를 session에 set해줘야함.
		session.setAttribute("loginUser", updatedUser);

		responseEntity = new ResponseEntity("MOD_SUCCEEDED", HttpStatus.OK);
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




