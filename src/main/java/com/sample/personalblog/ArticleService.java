package com.sample.personalblog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ArticleService {
	private final ArticleRepository articleRepository;

	public List<Article> getArticles() {
		return articleRepository.getArticles();
	}

	public Article getArticle(Long id) {
		return articleRepository.getArticle(id)
			.orElseThrow(() -> new NoSuchElementException());
	}

	public void createArticle(ArticleCreateRequest request) {
		LocalDateTime now = LocalDateTime.now();

		Article article = new Article(
			null,
			request.getTitle(),
			request.getContent(),
			now
		);

		articleRepository.create(article);
	}

	public void editArticle(Long id, ArticleEditRequest request) {
		Article article = articleRepository.getArticle(id)
			.orElseThrow(() -> new NoSuchElementException());

		article.setTitle(request.getTitle());
		article.setContent(request.getContent());

		articleRepository.edit(article);
	}

	public void deleteArticle(Long id) {
		articleRepository.delete(id);
	}
}
