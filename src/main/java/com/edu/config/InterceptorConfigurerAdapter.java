package com.edu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.edu.interceptor.SessionInterceptor;

@Configuration
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
//		registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**").excludePathPatterns("/js/*")
//				.excludePathPatterns("/css/*").excludePathPatterns("/images/*").excludePathPatterns("/fonts/*");
	}
}
