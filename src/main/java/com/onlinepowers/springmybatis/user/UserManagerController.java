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
@RequestMapping("/opmanager/user")
@Controller
public class UserManagerController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(User user) {

		return "/opmanager/user/login";
	}

	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");
			return "redirect:/opmanager/user/login";
		}

		//입력한 비밀번호를 해시함수로
		String hashPassword = SHA256Util.getEncrypt(user.getPassword(), loginUser.getId());
		log.debug(hashPassword);
		log.debug(loginUser.getPassword());

		//비밀번호 일치하지않으면
		if (!loginUser.getPassword().equals(hashPassword)) {
			log.debug("비밀번호 일치하지 않음");
			return "redirect:/opmanager/user/login";
		}
		//관리자가 아닐때
		if ("ROLE_USER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 관리자가 아닙니다.");
			return "redirect:/opmanager/user/login";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		return "redirect:/opmanager";
	}


	@GetMapping("/list")
	public String getUserList(@ModelAttribute("cri") Criteria cri,
	                          User user,
	                          HttpSession session, Model model) {

		List<User> userList = userService.getUserList(user, cri);
		model.addAttribute("userList", userList);

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/list";
	}

	@GetMapping("/create")
	public String registerForm(User user, HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("authority", loginUser.getUserRole().getAuthority());

		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/form";
	}

	@PostMapping("/create")
	public String createUser(@ModelAttribute("cri") Criteria cri,
	                         User user, UserDetail userDetail, UserRole userRole,
	                         HttpSession session, Model model) {

		user.setUserDetail(userDetail);
		user.setUserRole(userRole); //th:object = user , name이 authority인 태그 받음
		userService.insertUser(user);

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);

		//관리자페이지에서 등록하는 경우
		return "redirect:/opmanager/user/list";

	}


	@GetMapping("/edit/{id}")
	public String updateForm(@PathVariable("id") long id,
	                         @ModelAttribute("cri") Criteria cri,
	                         User user,
	                         HttpSession session, Model model) {

		user = userService.getUserById(id);
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		//세션 저장 정보
		User loginUser = (User) session.getAttribute("loginUser");

		//관리자일때만 목록 링크, 권한 수정 보이도록한다.
		String authority = loginUser.getUserRole().getAuthority();
		if ("ROLE_OPMANAGER".equals(authority)) {
			model.addAttribute("authority", authority);   //form 뷰에서 id있을때로 처리됨.
		}

		model.addAttribute("loginUser", loginUser);

		return "/opmanager/user/form";
	}

	@PostMapping("/edit/{id}")
	public String updateUser(@ModelAttribute("cri") Criteria cri,
	                         User user, UserDetail userDetail, UserRole userRole,
							 RedirectAttributes rttr, HttpSession session, Model model) {

		//user.getLoginId 로 입력받은값이 loginUser.
		User loginUser = (User) session.getAttribute("loginUser");

		//하위 테이블 수정안되는 현상 해결
		userDetail.setUserId(user.getId());
		userRole.setUserId(user.getId());

		user.setUserDetail(userDetail);
		user.setUserRole(userRole);

		userService.updateUser(user);

		if (user.getPassword() == "") {
			log.debug("비밀번호 공란");
			return "redirect:/opmanager/user/edit/" + user.getId();
		}

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/opmanager/user/list";

	}

	/**
	 * 삭제
	 * @param cri
	 * @param rttr
	 * @param model
	 * @return
	 */
	@PostMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, @ModelAttribute("cri") Criteria cri,
	                         RedirectAttributes rttr, Model model) {

		userService.deleteUserById(id);

		//redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/opmanager/user/list";
	}


}