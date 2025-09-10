package com.sample.personalblog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ArticleController {
	private final List<Article> articles = getArticles();

	@GetMapping("home")
	public String home(Model model) {
		model.addAttribute("articles", articles);
		return "home";
	}

	@GetMapping("article/{id}")
	public String article(@PathVariable Long id, Model model) {
		Article article = articles.get(id.intValue() - 1);
		model.addAttribute("article", article);

		return "article";
	}

	private List<Article> getArticles() {
		LocalDateTime now = LocalDateTime.now();

		return List.of(
			new Article(1L, "first article", "content 1", now),
			new Article(2L, "second article", "content 2", now),
			new Article(3L, "third article", "content 3", now)
		);
	}
}
