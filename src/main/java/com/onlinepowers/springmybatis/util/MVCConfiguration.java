package com.onlinepowers.springmybatis.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {

	public void addInterceptors(InterceptorRegistry registry) {

		Intercepter intercepter = new Intercepter();

		//user일때 add하면 통과가능.
		registry.addInterceptor(intercepter)
				.addPathPatterns("/opmanager")      //user일때 통과불가
				.addPathPatterns("/opmanager/user/list")
				.addPathPatterns("/opmanager/user/edit/*")
				.addPathPatterns("/opmanager/user/create/*")
				.excludePathPatterns("/user/login")       //user일때 통과가능
				.excludePathPatterns("/user/password-check")
				.excludePathPatterns("/opmanager/user/edit/" +3);  //3일때만 edit창 뜨는것 성공. 자신의 id값으로 치환

//				.excludePathPatterns("/opmanager/user/form")
//				.excludePathPatterns("/opmanager/user/list")
//				.excludePathPatterns("/opmanager/user/create");

		UserLoginIntercepter userLoginIntercepter = new UserLoginIntercepter();

		registry.addInterceptor(userLoginIntercepter)
				.addPathPatterns("/user/password-check")  //로그인 안된상태일때 로그인창으로 돌려보냄
				.addPathPatterns("/user/login")
				.addPathPatterns("/user/regist-form")
				.excludePathPatterns("/")
				.excludePathPatterns("/user/login");

		ManagerLoginIntercepter managerLoginIntercepter = new ManagerLoginIntercepter();

		registry.addInterceptor(managerLoginIntercepter)
				.addPathPatterns("/opmanager/user/form")     //로그인 안된상태일때 로그인창으로 돌려보냄
				.addPathPatterns("/opmanager/user/list")
				.addPathPatterns("/opmanager/user/edit/*")
				.addPathPatterns("/opmanager/user/create/*")
				.excludePathPatterns("/opmanager/user/login");

	}


}

