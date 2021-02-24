package com.onlinepowers.springmybatis.user;

import com.onlinepowers.springmybatis.paging.Criteria;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	//목록
	@RequestMapping("/user/list")
	public String getUserList(@ModelAttribute("user") User user, Model model) {
		List<User> userList = userService.getUserList(user);

		model.addAttribute("userList", userList);

		return "/user/list";
	}

	//수정, 글쓰기 합친 폼으로 이동
	@RequestMapping("/user/form")
	public String register(HttpServletRequest request, User user, UserDetail userDetail,
	                       @ModelAttribute("cri") Criteria cri, Model model) {
		String id = request.getParameter("id");

		if( id ==""){  //등록하기 창으로 넘어오기 (성공)
			model.addAttribute("user", user);
			model.addAttribute("userDetail", userDetail);

			return "/user/form";

		}else {  //수정하기
			int id1 = Integer.parseInt(id);
			System.out.println(id1); //값 넘어옴

			User user1 = userService.getUser(id1);
			BeanUtils.copyProperties(user1, user);       //User1를 user에 복사
			model.addAttribute("user", user);  //뷰에서 밸류값 지정하면 기존아이디 뜸

			return "/user/form";
		}
	}

	//게시글 등록 , 수정한것 등록
	@RequestMapping("/registerUser")
	public String registerUser(HttpServletRequest request, @ModelAttribute("cri") Criteria cri,
	                           @ModelAttribute("user") User user,@ModelAttribute("userDetail") UserDetail userDetail, Model model, RedirectAttributes rttr) {

		String id = request.getParameter("id");

		if( id =="" || id==null) {  //아이디값이 주소창에 없으면 insert한다.
			userService.insertUser(user, userDetail);

		}else {  //아이디값이 주소창에 있으면 update한다.
			//새로 받아온 값으로 치환한다. (th:value로 이전입력값 띄우므로 바꾸면 이전값들로 저장되어서) ??????
			String userName = request.getParameter("userName");
			String loginId =request.getParameter("loginId");
			String userPw =request.getParameter("userPw");
			String userEmail =request.getParameter("userEmail");
			String zipcode =request.getParameter("zipcode");
			String address =request.getParameter("address");
			String addressDetail =request.getParameter("addressDetail");
			String phoneNumber =request.getParameter("phoneNumber");
			String receiveSms =request.getParameter("receiveSms");

			User user1 = new User();
			user1.setUserName( userName);
			user1.setUserName(loginId);
			user1.setUserName(userPw);
			user1.setUserName( userEmail);
			user1.setUserName( zipcode);
			user1.setUserName(address);
			user1.setUserName( addressDetail);
			user1.setUserName(phoneNumber);
			user1.setUserName(receiveSms);

			userService.updateUser(user1);
			System.out.println("수정쿼리문돌림");

			rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
			rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
			rttr.addAttribute("pageSize", cri.getPageSize());
			rttr.addAttribute("searchType", cri.getSearchType());
			rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		}

		return "redirect:/user/list";
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

		return "redirect:/user/list";
	}

	 /*
     돌아가기
     redirect : 컨트롤러에서 뷰로 주소창에 연결된 값 보낼때 사용
     */
	@RequestMapping("/user/return")
	String goToList1(@ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {

		rttr.addAttribute("currentPageNo", cri.getCurrentPageNo());
		rttr.addAttribute("recordsPerPage", cri.getRecordsPerPage());
		rttr.addAttribute("pageSize", cri.getPageSize());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("searchKeyword", cri.getSearchKeyword());

		return "redirect:/user/list";
	}

	@ResponseBody
	@RequestMapping(value = "/user/idCheck", method = RequestMethod.POST)
	public int postIdCheck(HttpServletRequest request, User user) throws Exception {
		System.out.print("에이젝스");
		String loginId = request.getParameter("loginId");
		System.out.println(loginId);   //넘어옴

		int idCheck = userService.idCheck(loginId); //오류

		int result = 0;

		//MEMBERDTO를 반환.  있으면 1으로 반환
		if(idCheck != 0) {
			result = 1;
		}

		return result;
	}



}
