package com.onlinepowers.springmybatis.api.main;

import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainApiController {


	/**
	 * 유저 메인페이지
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/")
	public ModelAndView userMain(HttpSession session, Model model,
								 @AuthenticationPrincipal LoginUserDetails user) {

		ModelAndView mv = new ModelAndView();

		//세션처리 없애려면, 인터셉터, userUtils도 같이없애야한다.
		User loginUser = UserUtils.getLoginUser(session);

		if (loginUser == null) {
			mv.setViewName("/main/user");
			log.debug("=================널 " + user);
			return mv;
		}

		if (UserUtils.isManagerLogin(session)) {    //로그인 안되있을시 null 뜸
			session.invalidate();
			mv.setViewName("/main/user");
			log.debug("=================매니저 " + user);
			return mv;
		}

		if (UserUtils.isUserLogin(session)) {       //시큐리티 적용후 유저 로그인, 여기까지 넘어옴
			mv.setViewName("/main/user");
			log.debug("================= " + user);			//로그인 컨트롤러를 거쳐 인증된 정보 뜸.
			return mv;
		}

		mv.setViewName("/main/user");
		return mv;
	}


	/**
	 * 유저 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/user/logout")
	public ResponseEntity<String> userLogout(HttpSession session) {

		ResponseEntity<String> responseEntity = null;
		session.invalidate();
		responseEntity = new ResponseEntity("logout", HttpStatus.OK);
		return responseEntity;
	}


	/**
	 * 관리자 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/opmanager/logout")
	public ResponseEntity<String> managerLogout(HttpSession session) {

		ResponseEntity<String> responseEntity = null;
		session.invalidate();
		responseEntity = new ResponseEntity("logout", HttpStatus.OK);
		return responseEntity;
	}

}