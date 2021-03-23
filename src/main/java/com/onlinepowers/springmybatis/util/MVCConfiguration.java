package com.onlinepowers.springmybatis.util;

import com.onlinepowers.springmybatis.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {

	public void addInterceptors(InterceptorRegistry registry) {

		LoginInterceptor loginInterceptor = new LoginInterceptor();	//로그인x일시 접근x

		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/opmanager/user/**")
				.addPathPatterns("/user/**")
				.excludePathPatterns("/opmanager")
				.excludePathPatterns("/opmanager/user/login")
				.excludePathPatterns("/")
				.excludePathPatterns("/user/login")
				.excludePathPatterns("/user/create")
				.excludePathPatterns("/user/check-id");


		UserRoleInterceptor userRoleInterceptor = new UserRoleInterceptor();	//user일때 관리자 접근x

		registry.addInterceptor(userRoleInterceptor)
				.addPathPatterns("/opmanager/**")
				.excludePathPatterns("/opmanager")
				.excludePathPatterns("/opmanager/user/login");


		UserRoleEditInterceptor userRoleEditInterceptor = new UserRoleEditInterceptor();  //user일때 다른사람것 수정x

		registry.addInterceptor(userRoleEditInterceptor)
				.addPathPatterns("/user/edit/**");

	}

}

