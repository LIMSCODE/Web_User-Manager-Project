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
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(User user) {

		return "/user/login";
	}

	/**
	 * 로그인 버튼 누름
	 * @param user
	 * @return
	 */
	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		//입력한 비밀번호를 해시함수로
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//입력한 id에 해당하는 유저가 없으면
		if(loginUser == null) {
			
			log.debug("해당 아이디 없음");
			return "redirect:/user/login";
		}

		//가져온 비밀번호가 입력한 비밀번호를 해시함수 적용한 것과 일치하지 않으면
		if (!loginUser.getPassword().equals(hashPassword)) {

			log.debug("비밀번호 일치하지 않음");
			return "redirect:/user/login";
		}

		//비밀번호 일치시 세션에 담는다.
		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		model.addAttribute("loginUser", loginUser);

		return "redirect:/";
	}

	//로그인 후 수정하려할때 비밀번호 확인
	@GetMapping("/password-check")
	public String checkPassword() {

		return "/user/password-check";
	}

	@PostMapping("/password-check")
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

			log.debug("비밀번호 일치, 수정폼으로 이동");

			model.addAttribute("userRole",loginUser.getUserRole());

			return "redirect:/opmanager/user/edit/" + loginUser.getId();

		} else {

			log.debug("비밀번호 일치하지 않음 컨트롤러");

			return "redirect:/user/password-check";
		}

	}

	@PostMapping("/user/edit/{id}")
	public String updateUser(@ModelAttribute("cri") Criteria cri, User user,
	                         UserDetail userDetail, UserRole userRole, Model model,
	                         RedirectAttributes rttr, HttpSession session) {

		//user.getLoginId 로 입력받은값이 loginUser.
		User loginUser = (User) session.getAttribute("loginUser");

		//하위 테이블 수정안되는 현상 해결
		userDetail.setUserId(user.getId());
		userRole.setUserId(user.getId());

		user.setUserDetail(userDetail);
		user.setUserRole(userRole);

		if (user.getPassword() != ""
							|| "0".equals(loginUser.getUserRole().getAuthority())) {

			try {
				log.debug("수정한다.");
				userService.updateUser(user);

			} catch (Exception e) {

				log.debug("update 오류");
			}

			rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
			rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
			rttr.addAttribute("pageSize", cri.getPageSize());
			rttr.addAttribute("searchType", cri.getSearchType());
			rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

			return "redirect:/";

		} else {
			log.debug("수정되지않음");

			return "redirect:/opmanager/user/edit/" + user.getId();
		}

	}





}