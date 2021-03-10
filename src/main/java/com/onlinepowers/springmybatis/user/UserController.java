package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.util.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
public class UserController {
	@Autowired
	private UserService userService;


	//로그인 후 수정하려할때 비밀번호 확인
	@GetMapping("/user/password-check")
	public String checkPassword() {

		return "/user/password-check";
	}

	//수정폼 전에 비밀번호 확인하기
	@PostMapping("/user/password-check")
	public String checkPassword(User user, HttpSession session, Model model) {
		
		//세션 저장 정보
		User loginUser = (User) session.getAttribute("loginUser");
		
		//입력받은 비밀번호
		log.debug(user.getPassword());

		//입력한 비밀번호를 PK이용하여 해시함수로 만들고
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());

		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//로그인 세션의 비밀번호 값과 일치하는지 확인
		if (hashPassword.equals(loginUser.getPassword())) {

			log.debug("비밀번호 일치함 수정폼으로 이동");

			return "redirect:/user/edit/" + loginUser.getId();

		} else {

			log.debug("비밀번호 일치하지 않음 컨트롤러");

			return "redirect:/user/password-check";
		}

	}

	//수정하기 누르면 edit폼으로 넘어와서 (뷰에서 폼태그 히든값으로 세션안의 id를 같이넘김)
	@GetMapping("/user/edit/{id}")
	public String updateForm(@PathVariable("id") long id, Model model, HttpSession session) {

		//id값은 로그인세션의 id이고, model로 뷰로 보낸다.
		//세션 저장 정보
		User loginUser = (User) session.getAttribute("loginUser");

		model.addAttribute("user", loginUser);  //뷰에서 밸류값 지정하면 기존아이디 뜸

		return "/user/form";
	}

	/**
	 * 수정버튼 누름
	 * @param user
	 * @param userDetail
	 * @return
	 */
	@PostMapping("/user/edit/{id}")
	public String updateUser(@ModelAttribute("user") User user,
							 @ModelAttribute("userDetail") UserDetail userDetail) {

		userDetail.setUserId(user.getId());     //Detail테이블 수정안되는 현상 해결
		user.setUserDetail(userDetail);

		//입력된 정보를 받아와 update문 돌린다.
		userService.updateUser(user);

		return "redirect:/user/edit/" + user.getId();

	}

}