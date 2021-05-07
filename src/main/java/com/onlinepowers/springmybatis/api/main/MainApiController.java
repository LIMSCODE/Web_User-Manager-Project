package com.onlinepowers.springmybatis.api.main;

import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MainApiController {


	/**
	 * JWT에 담긴 권한 확인
	 * @param user
	 * @return
	 */
	@GetMapping("/checkJWT")
	public String jwt(Principal user){

		//권한체크 예시 - 로그인시 발급받은 토큰에서 온 것 ,
		//혹은 @AuthenticationPrincipal LoginUserDetails securityUser 로 컨트롤러에서 제어한다.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		LoginUserDetails securityUser = (LoginUserDetails) authentication.getPrincipal();
		String userAuthority = securityUser.getUser().getUserRole().getAuthority();

		if (!"ROLE_USER".equals(userAuthority)) {
			log.debug("권한 체크");
		}

		//만약 로그인되지 않은상태면 Filter에서 걸린다.
		//만약 권한이 다르면, 인터셉터에서 말고 필터에 걸리는지 확인해보자.
		return  securityUser.getUser().getUserRole().getAuthority() + " / " + securityUser.getUser().getPassword();
	}


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

		if (UserUtils.isUserLogin(session)) {       //시큐리티 적용후 유저 로그인
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