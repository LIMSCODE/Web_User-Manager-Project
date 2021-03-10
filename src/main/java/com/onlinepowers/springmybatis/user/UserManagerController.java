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
public class UserManagerController {
	@Autowired
	private UserService userService;

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
	 * 등록 버튼 누름
	 * @param cri
	 * @param user
	 * @param userDetail
	 * @return
	 */
	@PostMapping("/opmanager/user/create")
	public String createUser(Criteria cri,
	                         User user, UserDetail userDetail, UserRole userRole) {

		user.setUserDetail(userDetail);
		user.setUserRole(userRole); //th:object = user , name이 authority인 태그 (userRole으로 받아짐)
									//user안에 userRole을 set해줘서 넘어온 name=authority 데이터를 받는다.

		userService.insertUser(user);	//user에 userDetail 포함시켜서 매퍼로 넘김.

		return "redirect:/opmanager/user/list";
	}

	@GetMapping("/opmanager/user/edit/{id}")
	public String updateForm(@PathVariable("id") long id, User user,
	                         @ModelAttribute("cri") Criteria cri, Model model) {

		user = userService.getUserById(id);
		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸
		model.addAttribute("id", id);   //form 뷰에서 id있을때로 처리됨.

		return "/user/form";
	}

	/**
	 * 수정 버튼 누름
	 * @param cri
	 * @param user
	 * @param userDetail
	 * @param model
	 * @param rttr
	 * @return
	 */
	@PostMapping("/opmanager/user/edit/{id}")
	public String updateUser(Criteria cri, User user,
							UserDetail userDetail, UserRole userRole, Model model, RedirectAttributes rttr) {

		userDetail.setUserId(user.getId());     //Detail테이블 수정안되는 현상 해결

		user.setUserDetail(userDetail);
		user.setUserRole(userRole);

		//비밀번호가 널이 아니면 비번+다른정보 업데이트
		//비밀번호가 널이면 회피
		if (user.getPassword() != "") {
			log.debug("수정한다.");
			userService.updateUser(user);

			rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
			rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
			rttr.addAttribute("pageSize", cri.getPageSize());
			rttr.addAttribute("searchType", cri.getSearchType());
			rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

			return "redirect:/opmanager/user/list" ;

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
	public String deleteUser(@PathVariable("id") long id,
	                  @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr, Model model) {

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
	@PostMapping(value = "/opmanager/user/check-id")
	public int checkId(HttpServletRequest request, User user) throws Exception {

		String loginId = request.getParameter("loginId");
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류

		return userCount > 0 ? 1 : 0 ;
	}

}