package com.sample.personalblog.auth;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.sample.personalblog.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserRepository userRepository;

	public boolean authenticate(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		return userRepository.getUser(username)
			.map(user -> Objects.equals(user.getPassword(), password))
			.orElse(false);
	}
}
