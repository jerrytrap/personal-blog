package com.example.personalblog.article;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Article {
	private UUID id;
	private String title;
	private String content;
	private LocalDateTime publishedDate;

	public Article(String title, String content) {
		this.id = UUID.randomUUID();
		this.title = title;
		this.content = content;
		this.publishedDate = LocalDateTime.now();
	}

	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
