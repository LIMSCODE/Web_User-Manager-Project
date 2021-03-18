package com.onlinepowers.springmybatis.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {

	public void addInterceptors(InterceptorRegistry registry) {


		UserRoleIntercepter userRoleIntercepter = new UserRoleIntercepter();

		registry.addInterceptor(userRoleIntercepter)
				.addPathPatterns("/opmanager/**")
				.addPathPatterns("/user/edit/**");

	}



}

