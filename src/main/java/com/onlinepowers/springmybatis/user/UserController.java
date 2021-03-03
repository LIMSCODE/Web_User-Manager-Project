package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import com.onlinepowers.springmybatis.paging.PaginationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/user/list")
	public String getUserList(@ModelAttribute("user") User user, @ModelAttribute("cri") Criteria cri, Model model) {

		List<User> userList = userService.getUserList(user, cri);

		model.addAttribute("userList", userList);

		return "/user/list";
	}

	/**
	 * 글쓰기 폼으로 이동
	 * @param user
	 * @param cri
	 * @param model
	 * @return
	 */
	@GetMapping("/user/create")
	public String registerForm(User user, @ModelAttribute("cri") Criteria cri, Model model) {

		model.addAttribute("user", user);

		return "/user/form";
	}

	/**
	 * 등록 버튼 누름
	 * @param request
	 * @param cri
	 * @param user
	 * @param userDetail
	 * @param model
	 * @param rttr
	 * @return
	 */
	@PostMapping("/user/create")
	public String createUser(HttpServletRequest request, @ModelAttribute("cri") Criteria cri,
	                         @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail, Model model, RedirectAttributes rttr) {

		String id = request.getParameter("id");

		if (id == null) {
			user.setUserDetail(userDetail);
			userService.insertUser(user);	//user에 userDetail 포함시켜서 매퍼로 넘김.
		}

		return "redirect:/user/list";
	}


	/**
	 * 수정 폼으로 이동
	 * @param userDetail
	 * @param cri
	 * @param model
	 * @return
	 */
	@GetMapping("/user/edit")
	public String editForm( User user, UserDetail userDetail,
	                        @ModelAttribute("cri") Criteria cri, Model model) {

		long id = user.getId();

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
	@PostMapping("/user/edit")
	public String editUser(@ModelAttribute("cri") Criteria cri,
	                       @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail, Model model, RedirectAttributes rttr) {


		userDetail.setUserId(user.getId());     //Detail테이블 수정안되는 현상 해결
		user.setUserDetail(userDetail);

		userService.updateUser(user);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/user/list";
	}
	/**
	 * 삭제
	 * 	redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용

	 * @param cri
	 * @param rttr
	 * @param model
	 * @return
	 */
	@PostMapping(value = "/delete")
	String deleteUser(@RequestParam("id") String stringId,
	                  @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr, Model model) {

		int id= Integer.parseInt(stringId);
		userService.deleteUserById(id);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/user/list";
	}

	@ResponseBody
	@PostMapping(value = "/user/check-id")
	public int checkId(HttpServletRequest request, User user) throws Exception {

		String loginId = request.getParameter("loginId");
		log.debug(loginId);

		int userCount = userService.getUserCountByLoginId(loginId); //오류

		return userCount > 0 ? 1:0 ;
	}

}