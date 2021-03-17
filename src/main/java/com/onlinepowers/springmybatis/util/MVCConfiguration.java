package com.onlinepowers.springmybatis.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {

	public void addInterceptors(InterceptorRegistry registry) {

		//유저일때
		UserRoleIntercepter userRoleIntercepter = new UserRoleIntercepter();

		registry.addInterceptor(userRoleIntercepter)
				.addPathPatterns("/opmanager");   //user일때 통과불가



	}

}

