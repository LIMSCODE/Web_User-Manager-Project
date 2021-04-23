package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.paging.JpaPaging;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestController
@RequestMapping("/api/opmanager/user")
@RequiredArgsConstructor
public class UserManagerApiController {

	private final UserService userService;

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

			responseEntity = new ResponseEntity("다시입력해주세요",HttpStatus.BAD_REQUEST);

			return responseEntity;
			//return "/opmanager/user/form";
		}

		//입력받은 아이디에 해당하는 DTO값이 db에 있으면 insert안되도록
		User storedUser = userService.getUserByLoginId(user.getLoginId());
		if (storedUser != null) {
			log.debug("해당아이디 존재");

			responseEntity = new ResponseEntity("해당아이디 존재",HttpStatus.BAD_REQUEST);

			return responseEntity;
			//return "redirect:/opmanager/user/create";
		}

		userService.insertUser(user);

		User loginUser = UserUtils.getLoginUser(session);
		model.addAttribute("loginUser", loginUser);

		responseEntity = new ResponseEntity("ADD_SUCCEEDED",HttpStatus.OK);
		//관리자페이지에서 등록하는 경우
		//return "redirect:/opmanager/user/list";
		return responseEntity;

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
		//return "redirect:/opmanager/user/list";

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

		/*
		URI list = new URI("http://localhost:8080/opmanager/user/list");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(list);

		responseEntity = new ResponseEntity(httpHeaders, HttpStatus.OK);
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




