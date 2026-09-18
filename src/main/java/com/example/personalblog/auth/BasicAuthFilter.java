package com.example.personalblog.auth;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "auth.mode", havingValue = "basic")
public class BasicAuthFilter extends OncePerRequestFilter {
	private final AuthService authService;
	private static final String BASIC_AUTH_PREFIX = "Basic ";

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		String path = request.getRequestURI();

		if (!path.startsWith("/admin")) {
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

		if (authHeader == null || !authHeader.startsWith(BASIC_AUTH_PREFIX)) {
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
		String base64Credentials = authHeader.substring(BASIC_AUTH_PREFIX.length()).trim();

		byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		String credentials = new String(credDecoded, StandardCharsets.UTF_8);

		String[] values = credentials.split(":", 2);

		if (values.length != 2) {
			throw new IllegalArgumentException("Invalid Basic authentication format");
		}

		return new LoginRequest(values[0], values[1]);
	}
}
