package com.onlinepowers.springmybatis.api.user;

import com.onlinepowers.springmybatis.jwt.JwtTokenProvider;
import com.onlinepowers.springmybatis.security.LoginUserDetails;
import com.onlinepowers.springmybatis.user.User;
import com.onlinepowers.springmybatis.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/api/opmanager/user")
@CrossOrigin(origins="*", maxAge = 3600)
public class ManagerAuthenticationController {

	@Autowired
	private UserService userService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;


	/**
	 * 관리자 로그인
	 * @param user
	 * @param session
	 * @param model
	 * @return
	 */
	@PostMapping("/login")
	public String login(User user, HttpSession session, Model model) {

		//넘어온 아이디와 일치하는 정보를 모두 가져와 loginUser에 저장
		User loginUser = userService.getUserByLoginId(user.getLoginId());

		log.debug("-----------로그인 컨트롤러");

		//아이디가 널일때
		if (loginUser == null) {
			log.debug("아이디 안넘어옴");
			return "";
		}

		String storedPassword = loginUser.getPassword();
		log.debug(storedPassword);

		//비밀번호 일치하지않으면
		if (!passwordEncoder.matches(user.getPassword(), storedPassword)) {
			log.debug("비밀번호 일치하지 않음");
			return "";
		}

		//관리자가 아닐때
		if (!"ROLE_OPMANAGER".equals(loginUser.getUserRole().getAuthority())) {
			log.debug("권한이 사용자가 아닙니다.");
			return "";
		}

		session.setAttribute("loginUser", loginUser);
		session.setMaxInactiveInterval(1000 * 1000);

		// 아이디와 패스워드로, Security 가 알아 볼 수 있는 token 객체로 변경
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getLoginId(), user.getPassword());
		// AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리
		Authentication authentication = authenticationManager.authenticate(token);
		// 실제 SecurityContext 에 authentication 정보를 등록
		SecurityContextHolder.getContext().setAuthentication(authentication);

		//토큰 생성후 ajax로 쿠키에 저장, API요청시 자동으로 헤더에 포함시켜 보내짐
		return jwtTokenProvider.createToken(loginUser.getLoginId(), loginUser.getUserRole().getAuthority());
	}


	/**
	 * 관리자 로그아웃
	 * @param session
	 * @return
	 */
	@GetMapping("/logout")
	public ResponseEntity<String> managerLogout(HttpSession session) {

		ResponseEntity<String> responseEntity = null;
		session.invalidate();
		responseEntity = new ResponseEntity("logout", HttpStatus.OK);
		return responseEntity;
	}


}




