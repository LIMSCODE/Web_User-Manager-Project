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
public class UserManagerController {
	@Autowired
	private UserService userService;

	//매니저일때 메인
	@GetMapping("/opmanager")
	public String managerMain(User user, HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		//로그인여부 체크 Y-opmanager(관리자페이지) , N-opmanager/login로 보낸다.
		if (loginUser == null) {

			return "/opmanager/user/login";
		}

		model.addAttribute("loginUser", loginUser);

		return "/main/opmanager";
	}


	@GetMapping("/opmanager/user/login")
	public String login(User user) {

		return "/opmanager/user/login";
	}

	/**
	 * 로그인 버튼 누름
	 * @param user
	 * @return
	 */
	@PostMapping("/opmanager/user/login")
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
			return "redirect:/opmanager/user/login";
		}

		//가져온 비밀번호가 입력한 비밀번호를 해시함수 적용한 것과 일치하지 않으면
		if (!loginUser.getPassword().equals(hashPassword)) {

			log.debug("비밀번호 일치하지 않음");
			return "redirect:/opmanager/user/login";
		}

		//관리자일때
		if ("1".equals(loginUser.getUserRole().getAuthority())) {

			//비밀번호 일치시 세션에 담는다.
			session.setAttribute("loginUser", loginUser);
			session.setMaxInactiveInterval(1000 * 1000);

			model.addAttribute("loginUser", loginUser);

		}

		return "redirect:/main/opmanager";
	}

	@GetMapping("/opmanager/user/list")
	public String getUserList(User user, @ModelAttribute("cri") Criteria cri, Model model) {

		List<User> userList = userService.getUserList(user, cri);
		model.addAttribute("userList", userList);

		return "/opmanager/user/list";
	}

	@GetMapping("/opmanager/user/create")
	public String registerForm(User user) {

		return "/user/form";
	}

	/**
	 * main에서 회원가입, 관리자페이지에서 회원등록
	 * @param cri
	 * @param user
	 * @param userDetail
	 * @return
	 */
	@PostMapping("/opmanager/user/create")
	public String createUser(@ModelAttribute("cri") Criteria cri, User user,
	                         UserDetail userDetail, UserRole userRole,  HttpSession session) {

		user.setUserDetail(userDetail);
		user.setUserRole(userRole); //th:object = user , name이 authority인 태그 (userRole으로 받아짐)
									//user안에 userRole을 set해줘서 넘어온 name=authority 데이터를 받는다.

		userService.insertUser(user);

		//관리자페이지에서 등록하는 경우
		if (session.getAttribute("loginUser") != null) {

			return "redirect:/opmanager/user/list";

		//회원가입	
		} else {

			//관리자로 선택했을때
			if ("1".equals(user.getUserRole().getAuthority())) {

				log.debug("list로 보냄");

				//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
				User loginUser = userService.getUserByLoginId(user.getLoginId());
				session.setAttribute("loginUser", loginUser);

				return "redirect:/opmanager/user/list";

			//유저로 선택했을때
			} else {

				//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
				User loginUser = userService.getUserByLoginId(user.getLoginId());
				session.setAttribute("loginUser", loginUser);

				return "redirect:/user/after-login";
			}
		}
	}

	@GetMapping("/opmanager/user/edit/{id}")
	public String updateForm(@PathVariable("id") long id, User user,
	                         @ModelAttribute("cri") Criteria cri, Model model) {

		user = userService.getUserById(id);
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		//관리자일때만 목록 링크 보이도록한다.
		String authority = user.getUserRole().getAuthority();
		if ("1".equals(authority)) {
			model.addAttribute("authority", authority);   //form 뷰에서 id있을때로 처리됨.
		}

		return "/user/form";
	}

	@PostMapping("/opmanager/user/edit/{id}")
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

		if (user.getPassword() != "") {

			try {

				log.debug("수정한다.");
				userService.updateUser(user);

			} catch (Exception e) {

				log.debug("update 오류");
			}

				//관리자일때
				if ("1".equals(loginUser.getUserRole().getAuthority())) {

					rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
					rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
					rttr.addAttribute("pageSize", cri.getPageSize());
					rttr.addAttribute("searchType", cri.getSearchType());
					rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

					return "redirect:/opmanager/user/list";

				} else {

					return "redirect:/";
				}

		} else {
			log.debug("수정되지않음");

			return "redirect:/opmanager/user/edit/" + user.getId();
		}

	}

	/**
	 * 삭제
	 * @param cri
	 * @param rttr
	 * @param model
	 * @return
	 */
	@PostMapping("/opmanager/user/delete/{id}")
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

	@ResponseBody
	@PostMapping(value = "/check-id")
	public int checkId(User user) throws Exception {

		String loginId = user.getLoginId();
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류

		return userCount > 0 ? 1 : 0 ;
	}

}