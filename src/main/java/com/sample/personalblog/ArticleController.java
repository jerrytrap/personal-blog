package com.sample.personalblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ArticleController {
	private final ArticleService articleService;

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("articles", articleService.getArticles());

		return "home";
	}

	@GetMapping("/article/{id}")
	public String showArticle(@PathVariable Long id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "article";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("articles", articleService.getArticles());

		return "dashboard";
	}

	@GetMapping("/create")
	public String createForm() {
		return "create-form";
	}

	@PostMapping("/create")
	public String createArticle(@ModelAttribute ArticleCreateRequest request) {
		articleService.createArticle(request);

		return "redirect:/admin";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "edit-form";
	}

	@PostMapping("/edit/{id}")
	public String editArticle(@PathVariable Long id, @ModelAttribute ArticleEditRequest request) {
		articleService.editArticle(id, request);

		return "redirect:/admin";
	}

	@GetMapping("/delete/{id}")
	public String deleteArticle(@PathVariable Long id) {
		articleService.deleteArticle(id);

		return "redirect:/admin";
	}
}
