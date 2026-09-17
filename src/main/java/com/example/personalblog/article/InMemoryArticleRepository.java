package com.example.personalblog.article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryArticleRepository implements ArticleRepository {
	private final Map<UUID, Article> articles = new HashMap<>();

	@Override
	public List<Article> findAll() {
		return new ArrayList<>(articles.values());
	}

	@Override
	public Optional<Article> findById(UUID id) {
		return Optional.ofNullable(articles.get(id));
	}

	@Override
	public void save(Article article) {
		articles.put(article.getId(), article);
	}

	@Override
	public void deleteById(UUID id) {
		articles.remove(id);
	}
}
