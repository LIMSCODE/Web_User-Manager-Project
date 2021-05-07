package com.onlinepowers.springmybatis;

import com.onlinepowers.springmybatis.jwt.JwtAuthenticationFilter;
import com.onlinepowers.springmybatis.jwt.JwtEntryPoint;
import com.onlinepowers.springmybatis.jwt.JwtTokenProvider;
import com.onlinepowers.springmybatis.security.LoginUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity      // Spring Security를 활성화
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {   // Spring Security의 설정파일로서의 역할을 하기 위해 상속

	@Autowired
	private LoginUserDetailsService loginUserDetailsService; // 유저 정보를 가져올 클래스

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Bean
	public JwtEntryPoint jwtEntryPoint(){
		return new JwtEntryPoint();
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}


	/**
	 * 비밀번호 암호화
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {      //PasswordEncoder Bean을 등록 후 UserDetailsService에서 사용
		return new BCryptPasswordEncoder();
	}


	/**
	 * 인증을 무시할 경로
	 * @param web
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/webapp/content/common.css");	    //static 하위 폴더 (css, js, img)는 무조건 접근 가능
	}


	/**
	 * http 관련 인증 설정 (anyMatchers를 통해 경로 설정과 권한 설정)
	 * @param http
	 * @throws Exception
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
				.authorizeRequests()
					.antMatchers( "/", "/user/create", "/opmanager", "/user/check-id",
							"/user/login", "/opmanager/user/login").permitAll()     // 누구나 접근 허용
					.antMatchers("/user/**").hasRole("USER")             // user일경우에만 접근가능, 위에서 설정한부분은 예외.
					.antMatchers("/opmanager/**").hasRole("OPMANAGER")
		;


		//formLogin loginProcessingUrl - 로그인컨트롤러 안거치고 시큐리티가 처리
		//JWT토큰 사용시 로그인컨트롤러 거쳐야함 - formLogin 사용x
		/*
		http
				.formLogin()
				.loginProcessingUrl("/user/login")
				.loginPage("/user/login")
				.failureUrl("/guest/login?error")
				.defaultSuccessUrl("/", true)
				.usernameParameter("loginId")
				.passwordParameter("password")
				.permitAll();
		*/


		http.cors().and().csrf().disable()
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().authenticationEntryPoint(jwtEntryPoint());
				//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}


	/**
	 * 로그인할 때 필요한 정보를 가져오는 곳
	 * @param auth
	 * @throws Exception
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginUserDetailsService)        //UserDetailsService를 implements해서 loadUserByUsername() 구현
				.passwordEncoder(passwordEncoder()); 	        //패스워드 인코더는 위에서 빈으로 등록해놓은 passwordEncoder()를 사용

	}

}
