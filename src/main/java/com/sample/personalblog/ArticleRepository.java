package com.sample.personalblog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository {
	private final List<Article> articles = new ArrayList<>();

	public List<Article> getArticles() {
		return articles;
	}

	public Optional<Article> getArticle(Long id) {
		return articles.stream()
			.filter(article -> Objects.equals(article.getId(), id))
			.findFirst();
	}

	public void create(Article article) {
		article.setId(getLastId() + 1);
		articles.add(article);
	}

	public void edit(Article article) {}

	public void delete(Long id) {
		articles.removeIf(a -> Objects.equals(a.getId(), id));
	}

	private Long getLastId() {
		return articles.isEmpty() ? 1L: articles.get(articles.size() - 1).getId();
	}
}
