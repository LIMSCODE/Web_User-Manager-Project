package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping("/test")
	public String hello() {
		return "list";
	}

	//목록
	@RequestMapping("/list")
	public String getUserList(@ModelAttribute("user") UserDto user, Model model) {
		List<UserDto> userList = userService.getUserList(user);
		model.addAttribute("userList", userList);
		return "list";
	}

	//글쓰기 폼으로 이동
	@RequestMapping("/register")
	public String register() {
		return "register";
	}

	//게시글 등록
	@RequestMapping("/registerUser")
	public String registerUser(UserDto user) {
		userService.insertUser(user);
		return "redirect:/list";
	}

	// 수정 폼으로 이동
	@GetMapping(value = "/edit/{no}")
	public String edit(@PathVariable("no") Integer id, UserDto user,
	                   @ModelAttribute("cri") Criteria cri, Model model) {
		UserDto user1 = userService.getUser(id);
		BeanUtils.copyProperties(user1, user);       //User1를 user에 복사
		model.addAttribute("user", user);

		return "edit";
	}

	/*
	 최종 수정 버튼 누름
	 redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
	 */
	@GetMapping(value = "edit")
	public String updateUser(@RequestParam String id, @ModelAttribute("cri") Criteria cri,
	                         @ModelAttribute("user") UserDto user, Model model, RedirectAttributes rttr) {
		userService.updateUser(user);
		model.addAttribute("user", user);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/list";
	}

	/*
	삭제
	redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
	*/
	@GetMapping(value = "delete/{no}")
	String deleteUser(@PathVariable("no") Integer id, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		userService.deleteUser(id);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/list";
	}


	 /*
     돌아가기
     redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
     */
	@RequestMapping("/return")
	String goToList(@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/list";
	}

}
