package com.example.personalblog.auth;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@Order(1)
public class SessionAuthFilter extends OncePerRequestFilter {
	@Override
	public void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		String path = request.getRequestURI();
		boolean isLoggedIn = (session != null && session.getAttribute(SessionConstant.LOGIN_USER) != null);

		if (!path.startsWith("/admin")) {
			chain.doFilter(request, response);
			return;
		}

		if (path.equals("/login")) {
			if (isLoggedIn) {
				response.sendRedirect("/admin");
				return;
			}
			chain.doFilter(request, response);
			return;
		}

		if (!isLoggedIn) {
			response.sendRedirect("/login");
			return;
		}

		chain.doFilter(request, response);
	}
}
