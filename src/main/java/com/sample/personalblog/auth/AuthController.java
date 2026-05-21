package com.sample.personalblog.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
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
	public String login(@ModelAttribute LoginRequest loginRequest, HttpServletRequest request, Model model) {
		if (authService.authenticate(loginRequest)) {
			reissueSession(request, loginRequest.getUsername());
			return "redirect:/admin";
		} else {
			model.addAttribute("error", "Wrong ID or Password");
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	private void reissueSession(HttpServletRequest request, String username) {
		HttpSession oldSession = request.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();
		}

		HttpSession newSession = request.getSession(true);
		newSession.setAttribute(SessionConstant.LOGIN_USER, username);
	}
}
