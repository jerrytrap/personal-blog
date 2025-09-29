package com.sample.personalblog.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CustomAuthenticationFilter extends HttpFilter {
	@Override
	public void doFilter(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		String path = request.getRequestURI();

		if (path.startsWith("/home") || path.startsWith("/article") || path.startsWith("/login")) {
			chain.doFilter(request, response);
			return;
		}

		if (session == null || session.getAttribute("loggedInUser") == null) {
			response.sendRedirect("/login");
			return;
		}

		chain.doFilter(request, response);
	}
}
