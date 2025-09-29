package com.sample.personalblog.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	private final List<User> users = List.of(
		new User("admin", "1234")
	);

	public Optional<User> getUser(String username) {
		return users.stream()
			.filter(user -> Objects.equals(user.getUsername(), username))
			.findFirst();
	}
}
