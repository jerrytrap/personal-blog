package com.example.personalblog.article;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService {
	private final ArticleRepository articleRepository;

	public List<Article> getArticles() {
		return articleRepository.findAll();
	}

	public Article getArticle(UUID id) {
		return articleRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException());
	}

	public void createArticle(ArticleCreateRequest request) {
		Article article = new Article(request.getTitle(), request.getContent());

		articleRepository.save(article);
	}

	public void updateArticle(UUID id, ArticleUpdateRequest request) {
		Article article = getArticle(id);

		article.update(request.getTitle(), request.getContent());
		articleRepository.save(article);
	}

	public void deleteArticle(UUID id) {
		articleRepository.deleteById(id);
	}
}
