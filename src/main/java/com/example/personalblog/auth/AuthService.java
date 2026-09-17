package com.example.personalblog.auth;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.personalblog.admin.AdminRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public boolean authenticate(LoginRequest loginRequest) {
		String username = loginRequest.getUsername();
		String password = loginRequest.getPassword();

		return adminRepository.findByUsername(username)
			.map(user -> passwordEncoder.matches(password,user.getPassword()))
			.orElse(false);
	}
}

