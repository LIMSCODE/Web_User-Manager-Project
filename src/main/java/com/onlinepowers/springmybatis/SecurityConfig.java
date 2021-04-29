package com.onlinepowers.springmybatis;

import com.onlinepowers.springmybatis.jwt.JwtAuthenticationFilter;
import com.onlinepowers.springmybatis.jwt.JwtEntryPoint;
import com.onlinepowers.springmybatis.jwt.JwtTokenProvider;
import com.onlinepowers.springmybatis.user.LoginUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@EnableWebSecurity      // Spring Security를 활성화
@Configuration
class SecurityConfig extends WebSecurityConfigurerAdapter {   // Spring Security의 설정파일로서의 역할을 하기 위해 상속

	@Autowired
	private LoginUserDetailsService loginUserDetailsService; // 유저 정보를 가져올 클래스

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

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
				.authorizeRequests() 		// 접근에 대한 인증 설정
					.antMatchers( "/", "/user/login", "/user/create").permitAll() 	// 누구나 접근 허용
					.anyRequest().authenticated();

		/*
		http
				.formLogin()
				.loginProcessingUrl("/user/login")
				.loginPage("/user/login") 	// 로그인 페이지 링크
											//이걸 일치시키면 컨트롤러 진입안됨, 객체는 가져와짐
				.failureUrl("/guest/login?error")
				//.defaultSuccessUrl("/", true)
				.usernameParameter("loginId")
				.passwordParameter("password")
				.permitAll();
		*/

		http.cors().and().csrf().disable();
				//.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				//.and()
				//.exceptionHandling().authenticationEntryPoint(jwtEntryPoint)      //빈 등록이안되서
				//.and()
				//.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		//이거 지워야 시큐리티 로그인 컨트롤러url 동작하고, 객체도 받아와진다. 왜일까???
	}


	/**
	 * 로그인할 때 필요한 정보를 가져오는 곳
	 * @param auth
	 * @throws Exception
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginUserDetailsService)        // UserDetailsService를 implements해서 loadUserByUsername() 구현
				.passwordEncoder(passwordEncoder()); 	//패스워드 인코더는 위에서 빈으로 등록해놓은 passwordEncoder()를 사용

		System.out.println("시큐리티config");       //antMatcher설정하고 로그인했더니 여기까지 뜸
	}

}
