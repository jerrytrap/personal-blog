package com.example.personalblog.article;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.personalblog.auth.SessionConstant;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ArticleController {
	private final ArticleService articleService;

	@GetMapping("/")
	public String list(Model model) {
		model.addAttribute("articles", articleService.getArticles());

		return "articles/list";
	}

	@GetMapping("/articles/{id}")
	public String detail(@PathVariable UUID id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "articles/detail";
	}

	@GetMapping("/admin/create")
	public String createForm(HttpSession session, Model model) {
		model.addAttribute("csrfToken", session.getAttribute(SessionConstant.CSRF_TOKEN));
		return "articles/create";
	}

	@PostMapping("/admin/create")
	public String createArticle(@ModelAttribute ArticleCreateRequest request) {
		articleService.createArticle(request);

		return "redirect:/admin";
	}

	@GetMapping("/admin/update/{id}")
	public String updateForm(@PathVariable UUID id, HttpSession session, Model model) {
		model.addAttribute("article", articleService.getArticle(id));
		model.addAttribute("csrfToken", session.getAttribute(SessionConstant.CSRF_TOKEN));

		return "articles/update";
	}

	@PostMapping("/admin/update/{id}")
	public String updateArticle(@PathVariable UUID id, @ModelAttribute ArticleUpdateRequest request) {
		articleService.updateArticle(id, request);

		return "redirect:/admin";
	}

	@PostMapping("/admin/delete/{id}")
	public String deleteArticle(@PathVariable UUID id) {
		articleService.deleteArticle(id);

		return "redirect:/admin";
	}
}
