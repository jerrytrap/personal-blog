package com.example.personalblog.article;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleUpdateRequest {
	private String title;
	private String content;
}