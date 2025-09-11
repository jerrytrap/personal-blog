package com.sample.personalblog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreateRequest {
	private String title;
	private String content;
}
