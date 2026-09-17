package com.example.personalblog.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;
	private final String username;
	private final String password;

	public AdminInitializer(
		AdminRepository adminRepository,
		PasswordEncoder passwordEncoder,
		@Value("${admin.username}") String username,
		@Value("${admin.password}") String password
	) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
		this.username = username;
		this.password = password;
	}

	@Override
	public void run(String... args) {
		if (adminRepository.findByUsername(username).isEmpty()) {
			adminRepository.save(new Admin(username, passwordEncoder.encode(password)));
		}
	}
}
