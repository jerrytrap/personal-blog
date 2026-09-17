package com.example.personalblog.article;

import java.util.UUID;

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

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("articles", articleService.getArticles());

		return "list";
	}

	@GetMapping("/article/{id}")
	public String showArticle(@PathVariable UUID id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "detail";
	}

	@GetMapping("/create")
	public String createForm() {
		return "create";
	}

	@PostMapping("/create")
	public String createArticle(@ModelAttribute ArticleCreateRequest request) {
		articleService.createArticle(request);

		return "redirect:/";
	}

	@GetMapping("/update/{id}")
	public String updateForm(@PathVariable UUID id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "update";
	}

	@PostMapping("/update/{id}")
	public String updateArticle(@PathVariable UUID id, @ModelAttribute ArticleUpdateRequest request) {
		articleService.updateArticle(id, request);

		return "redirect:/";
	}

	@PostMapping("/delete/{id}")
	public String deleteArticle(@PathVariable UUID id) {
		articleService.deleteArticle(id);

		return "redirect:/";
	}
}
