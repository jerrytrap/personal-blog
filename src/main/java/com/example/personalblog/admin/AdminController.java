package com.example.personalblog.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.personalblog.article.ArticleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AdminController {
	private final ArticleService articleService;

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("articles", articleService.getArticles());

		return "admin/dashboard";
	}
}
