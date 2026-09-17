package com.example.personalblog.admin;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {
	private Admin admin;

	public Optional<Admin> findByUsername(String username) {
		if (admin != null && username.equals(admin.getUsername())) {
			return Optional.of(admin);
		} else {
			return Optional.empty();
		}
	}

	public void save(Admin admin) {
		this.admin = admin;
	}
}
