package com.sample.personalblog.auth;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SessionAuthenticationFilter extends HttpFilter {
	@Override
	public void doFilter(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		String path = request.getRequestURI();
		boolean isLoggedIn = (session != null && session.getAttribute(SessionConstant.LOGIN_USER) != null);

		if (path.equals("/") || path.startsWith("/article") || path.equals("/logout")) {
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
