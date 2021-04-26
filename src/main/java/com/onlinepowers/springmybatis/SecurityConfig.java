package com.onlinepowers.springmybatis;

import com.onlinepowers.springmybatis.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity      // Spring Security를 활성화
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {   // Spring Security의 설정파일로서의 역할을 하기 위해 상속

	private final UserService userService; // 유저 정보를 가져올 클래스

	/**
	 * 비밀번호 암호화
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {      //PasswordEncoder Bean을 등록 후 UserDetailsService에서 사용
		return new BCryptPasswordEncoder();
	}


	/**
	 * 인증을 무시할 경로
	 * @param web
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");	    //static 하위 폴더 (css, js, img)는 무조건 접근 가능
	}


	/**
	 * http 관련 인증 설정 (anyMatchers를 통해 경로 설정과 권한 설정)
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests() 		// 접근에 대한 인증 설정
				.antMatchers("/css/**", "/js/**", "/img/**", "/login", "/", "/opmanager").permitAll() 	// 누구나 접근 허용
				.antMatchers("/**").hasRole("ROLE_USER") 	                    // USER, ADMIN만 접근 가능 (특정 권한이 있는 사람)
				.antMatchers("/opmanager/**").hasRole("ROLE_OPMANAGER") 	        // ADMIN만 접근 가능
				.anyRequest().authenticated()		                                // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있으면 접근 가능
																					// anyRequest는 anyMatchers에서 설정하지 않은 나머지 경로
				.and()
				.formLogin() 	            // 로그인에 관한 설정
				.loginPage("/login") 	    // 로그인 페이지 링크
				.loginPage("/opmanager/user/login")
				.defaultSuccessUrl("/") 	// 로그인 성공 후 리다이렉트 주소
				.permitAll()

				.and()
				.logout() 		                // 로그아웃에 관한 설정
				.logoutSuccessUrl("/")		    // 로그아웃 성공시 리다이렉트 주소
				.invalidateHttpSession(true) 	// 세션 날리기
				.permitAll()
		;
	}


	/**
	 * 로그인할 때 필요한 정보를 가져오는 곳
	 * @param auth
	 * @throws Exception
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)            // userService에서는 UserDetailsService를 implements해서 loadUserByUsername() 구현
				.passwordEncoder(passwordEncoder()); 	//패스워드 인코더는 위에서 빈으로 등록해놓은 passwordEncoder()를 사용
	}

}
