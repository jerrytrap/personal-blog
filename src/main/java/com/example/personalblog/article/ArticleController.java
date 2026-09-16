package com.example.personalblog.article;

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
	public String showArticle(@PathVariable Long id, Model model) {
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

	@GetMapping("/edit/{id}")
	public String editForm(@PathVariable Long id, Model model) {
		model.addAttribute("article", articleService.getArticle(id));

		return "edit";
	}

	@PostMapping("/edit/{id}")
	public String editArticle(@PathVariable Long id, @ModelAttribute ArticleEditRequest request) {
		articleService.editArticle(id, request);

		return "redirect:/";
	}

	@PostMapping("/delete/{id}")
	public String deleteArticle(@PathVariable Long id) {
		articleService.deleteArticle(id);

		return "redirect:/";
	}
}
