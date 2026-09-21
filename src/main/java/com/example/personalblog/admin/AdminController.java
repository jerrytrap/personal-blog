package com.example.personalblog.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.personalblog.article.ArticleService;
import com.example.personalblog.auth.SessionConstant;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final ArticleService articleService;

	@GetMapping("/admin")
	public String admin(HttpSession session, Model model) {
		model.addAttribute("articles", articleService.getArticles());
		model.addAttribute("csrfToken", session.getAttribute(SessionConstant.CSRF_TOKEN));

		return "admin/dashboard";
	}
}
