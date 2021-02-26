package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
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

	//목록
	@GetMapping("/user/list")
	public String getUserList(@ModelAttribute("user") User user, Model model) {
		List<User> userList = userService.getUserList(user);
		model.addAttribute("userList", userList);

		return "/user/list";
	}

	//글쓰기 폼으로 이동
	@GetMapping("/user/create")
	public String registerForm(HttpServletRequest request, User user,
	                           @ModelAttribute("cri") Criteria cri, Model model) {

		model.addAttribute("user", user);

		int create = 1;
		model.addAttribute("create", create);

		return "/user/form";
	}

	//수정 폼으로 이동
	@GetMapping("/user/edit")
	public String editForm(HttpServletRequest request,  UserDetail userDetail,
	                       @ModelAttribute("cri") Criteria cri, Model model) {

		String id = request.getParameter("id");
		int id1 = Integer.parseInt(id);
		System.out.println(id1); //값 넘어옴

		model.addAttribute("id", id1);

		User user = userService.getUser(id1);

		model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸


		return "/user/form";
	}

	//게시글 등록 , 수정한것 등록
	@PostMapping("/registerUser")
	public String registerUser(HttpServletRequest request, @ModelAttribute("cri") Criteria cri,
	                           @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail, Model model, RedirectAttributes rttr) {
		String create = request.getParameter("create");
		int create1 = Integer.parseInt(create);
		String edit= request.getParameter("edit");
		int edit1 = Integer.parseInt(edit);

		if (create1==1) {
			userService.insertUser(user, userDetail);
		}
		if (edit1==1) {
			userDetail.setUserId(user.getId());     //Detail테이블 수정안되는 현상 해결

			if (user.getUserPw() == "") {	//Db에서 수정 회피
			}else {
				userService.updateUser(user, userDetail);
			}

			model.addAttribute("user", user);
		}

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/user/list";
	}


	/*
	삭제
	redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
	*/
	@PostMapping(value = "/delete")
	String deleteUser(@RequestParam("id") String id,
	                  @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr, Model model) {

		int id1= Integer.parseInt(id);
		userService.deleteUser(id1);

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/user/list";
	}


	@ResponseBody
	@PostMapping(value = "/user/checkId")
	public int IdCheck(HttpServletRequest request, User user) throws Exception {

		String loginId = request.getParameter("loginId");
		System.out.println(loginId);   //넘어옴
		log.debug(loginId);
		int idCheck = userService.checkId(loginId); //오류

		int result = 0;

		if(idCheck != 0) {
			result = 1;
		}

		return result;
	}

}