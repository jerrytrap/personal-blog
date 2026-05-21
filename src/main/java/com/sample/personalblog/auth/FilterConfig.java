package com.sample.personalblog.auth;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {
	private final AuthService authService;

	@Bean
	public FilterRegistrationBean<SessionAuthenticationFilter> sessionFilter() {
		FilterRegistrationBean<SessionAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SessionAuthenticationFilter());
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}

	//Basic 인증 시 사용
	/*@Bean
	public FilterRegistrationBean<BasicAuthenticationFilter> basicFilter() {
		FilterRegistrationBean<BasicAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new BasicAuthenticationFilter(authService));
		registrationBean.addUrlPatterns("/*");

		return registrationBean;
	}*/
}
