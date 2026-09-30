package com.example.personalblog.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.personalblog.admin.AdminRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminUserDetailsService implements UserDetailsService {
	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return adminRepository.findByUsername(username)
			.map(admin -> User.builder()
				.username(admin.getUsername())
				.password(admin.getPassword())
				.build())
			.orElseThrow(() -> new UsernameNotFoundException("잘못된 ID 또는 비밀번호입니다."));
	}
}