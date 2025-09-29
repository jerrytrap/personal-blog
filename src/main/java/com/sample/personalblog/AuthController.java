package com.sample.personalblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AuthController {
	private final AuthService authService;

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest loginRequest, HttpSession session, Model model) {
		try {
			authService.login(loginRequest);
			session.setAttribute("loggedInUser", loginRequest.getUsername());
			return "redirect:/admin";
		} catch (RuntimeException e) {
			model.addAttribute("error", "Wrong ID or Password");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/home";
	}
}
