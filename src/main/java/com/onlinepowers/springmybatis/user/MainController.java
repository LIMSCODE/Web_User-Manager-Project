package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class MainController {
	@Autowired
	private UserService userService;

	@GetMapping("/user/login")
	public String userLogin(@ModelAttribute("user") User user, @ModelAttribute("cri") Criteria cri, Model model) {

		List<User> userList = userService.getUserList(user, cri);
		model.addAttribute("userList", userList);

		return "user-after-login";
	}
	@GetMapping("/opmanager/user/login")
	public String userManagerLogin(@ModelAttribute("user") User user, @ModelAttribute("cri") Criteria cri, Model model) {

		List<User> userList = userService.getUserList(user, cri);
		model.addAttribute("userList", userList);

		return "user-after-login";
	}

	/**
	 * 로그인 버튼 누름
	 * @param cri
	 * @param user
	 * @param userDetail
	 * @return
	 */
	@PostMapping("/user/login")
	public String userLogin(@ModelAttribute("cri") Criteria cri,
	                         @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail) {

		user.setUserDetail(userDetail);
		userService.insertUser(user);	//user에 userDetail 포함시켜서 매퍼로 넘김.

		return "redirect:/user/after-login";
	}

	@PostMapping("/opmanager/user/login")
	public String userManagerLogin(@ModelAttribute("cri") Criteria cri,
	                         @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail) {

		user.setUserDetail(userDetail);
		userService.insertUser(user);	//user에 userDetail 포함시켜서 매퍼로 넘김.

		return "redirect:/user/list";
	}

}