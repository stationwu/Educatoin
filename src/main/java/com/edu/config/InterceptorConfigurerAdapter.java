package com.edu.config;

import com.edu.interceptor.WebAPIInterceptor;
import com.edu.web.rest.CustomerController;
import com.edu.web.rest.OrderController;
import com.edu.web.rest.WxJsSdkSignatureController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.edu.interceptor.SessionInterceptor;

@Configuration
public class InterceptorConfigurerAdapter extends WebMvcConfigurerAdapter {
	
	@Bean
	SessionInterceptor sessionInterceptor() {
         return new SessionInterceptor();
    }

    @Bean
	WebAPIInterceptor webAPIInterceptor() {
		return new WebAPIInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(sessionInterceptor())
				.addPathPatterns("/user/**")
				.excludePathPatterns("/user/signup")
				.excludePathPatterns("/user/center");
		registry.addInterceptor(webAPIInterceptor())
				.addPathPatterns("/api/**")
				.excludePathPatterns(CustomerController.SIGNUP_PATH)
				.excludePathPatterns(OrderController.PAYMENT_NOTIFY_PATH)
				.excludePathPatterns(OrderController.REFUND_NOTIFY_PATH)
				.excludePathPatterns(WxJsSdkSignatureController.PATH);
	}
}
