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
	public String registerForm(User user, @ModelAttribute("cri") Criteria cri, Model model) {

		model.addAttribute("user", user);

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
	public String createUser(@ModelAttribute("cri") Criteria cri,
	                         @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail) {

		user.setUserDetail(userDetail);
		userService.insertUser(user);	//user에 userDetail 포함시켜서 매퍼로 넘김.

		return "redirect:/user/list";
	}

	@GetMapping("/opmanager/user/edit/{id}")
	public String updateForm(@PathVariable("id") long id, User user, @ModelAttribute("cri") Criteria cri, Model model) {

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
	public String updateUser(@ModelAttribute("cri") Criteria cri, @ModelAttribute("user") User user,
							 @ModelAttribute("userDetail") UserDetail userDetail, Model model, RedirectAttributes rttr) {

		userDetail.setUserId(user.getId());     //Detail테이블 수정안되는 현상 해결
		user.setUserDetail(userDetail);

		if (userService.updateUser(user) == 1) {
			log.debug("비밀번호 일치 컨트롤러");
			userService.updateUser(user);

			rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
			rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
			rttr.addAttribute("pageSize", cri.getPageSize());
			rttr.addAttribute("searchType", cri.getSearchType());
			rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

			return "redirect:/opmanager/user/list" ;

		} else {
			log.debug("비밀번호 일치하지않음 컨트롤러");

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
	@PostMapping(value = "/opmanager/delete/{id}")
	String deleteUser(@PathVariable("id") long id,
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