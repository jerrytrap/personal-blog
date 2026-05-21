package com.sample.personalblog.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends HttpFilter {
	private final AuthService authService;

	@Override
	protected void doFilter(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		String path = request.getRequestURI();

		if (path.equals("/") || path.startsWith("/article") || path.equals("/logout")) {
			chain.doFilter(request, response);
			return;
		}

		if (tryBasicAuthentication(request)) {
			chain.doFilter(request, response);
			return;
		}

		response.setHeader("WWW-Authenticate", "Basic");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Access");
	}

	private boolean tryBasicAuthentication(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Basic ")) {
			return false;
		}

		try {
			LoginRequest loginRequest = extractCredentials(authHeader);
			return authService.authenticate(loginRequest);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private LoginRequest extractCredentials(String authHeader) {
		String base64Credentials = authHeader.substring(6).trim();

		byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		String credentials = new String(credDecoded, StandardCharsets.UTF_8);

		String[] values = credentials.split(":", 2);

		if (values.length != 2) {
			throw new IllegalArgumentException("Invalid Basic authentication format");
		}

		return new LoginRequest(values[0], values[1]);
	}
}
