package com.example.personalblog.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {
	@GetMapping("/login")
	public String loginForm() {
		return "admin/login";
	}
}
