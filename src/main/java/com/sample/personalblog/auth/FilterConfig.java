package com.sample.personalblog.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<CustomAuthenticationFilter> myFilter() {
		FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomAuthenticationFilter());
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}
}
