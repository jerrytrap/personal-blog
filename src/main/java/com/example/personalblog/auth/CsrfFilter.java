package com.example.personalblog.auth;

import java.io.IOException;
import java.util.Set;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@Order(2)
public class CsrfFilter extends OncePerRequestFilter {
	private static final Set<String> UNSAFE_METHODS = Set.of("POST", "PUT", "DELETE", "PATCH");

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws ServletException, IOException {
		if (!UNSAFE_METHODS.contains(request.getMethod()) || request.getRequestURI().equals("/login")) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = request.getSession(false);
		String sessionToken = (session != null) ? (String) session.getAttribute(SessionConstant.CSRF_TOKEN) : null;
		String requestToken = request.getParameter("csrfToken");

		if (sessionToken == null || !sessionToken.equals(requestToken)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
			return;
		}

		chain.doFilter(request, response);
	}
}
