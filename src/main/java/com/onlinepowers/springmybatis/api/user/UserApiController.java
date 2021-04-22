package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.api.ApiResponseEntity;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.SHA256Util;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
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




