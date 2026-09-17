package com.example.personalblog.article;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArticleRepository {
	List<Article> findAll();

	Optional<Article> findById(UUID id);

	void save(Article article);

	void deleteById(UUID id);
}
