package com.sample.personalblog.auth;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sample.personalblog.user.User;
import com.sample.personalblog.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final UserRepository userRepository;

	public void login(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();
		Optional<User> user = userRepository.getUser(username);

		if (user.isEmpty() || !Objects.equals(user.get().getPassword(), password)) {
			throw new RuntimeException();
		}
	}
}
