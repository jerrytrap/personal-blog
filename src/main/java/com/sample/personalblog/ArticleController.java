package com.sample.personalblog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ArticleController {
	private final List<Article> articles = new ArrayList<>();

	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("articles", articles);

		return "home";
	}

	@GetMapping("/article/{id}")
	public String showArticle(@PathVariable Long id, Model model) {
		Article article = articles.stream()
			.filter(a -> Objects.equals(a.getId(), id))
			.findFirst()
			.get();
		model.addAttribute("article", article);

		return "article";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		model.addAttribute("articles", articles);

		return "dashboard";
	}

	@GetMapping("/create")
	public String createForm() {
		return "create-form";
	}

	@PostMapping("/create")
	public String createArticle(@ModelAttribute ArticleCreateRequest request) {
		LocalDateTime now = LocalDateTime.now();
		long lastId = articles.isEmpty() ? 1L: articles.get(articles.size() - 1).getId();

		Article article = new Article(lastId + 1, request.getTitle(), request.getContent(), now);
		articles.add(article);

		return "redirect:/admin";
	}

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		Article article = articles.stream()
			.filter(a -> Objects.equals(a.getId(), id))
			.findFirst()
			.get();
		model.addAttribute("article", article);

		return "edit-form";
	}

	@PostMapping("/edit/{id}")
	public String editArticle(@PathVariable Long id, @ModelAttribute ArticleEditRequest request) {
		Article article = articles.stream()
			.filter(a -> Objects.equals(a.getId(), id))
			.findFirst()
			.get();

		article.setTitle(request.getTitle());
		article.setContent(request.getContent());

		return "redirect:/admin";
	}

	@GetMapping("/delete/{id}")
	public String deleteArticle(@PathVariable Long id) {
		articles.removeIf(a -> Objects.equals(a.getId(), id));

		return "redirect:/admin";
	}
}
