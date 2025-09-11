package com.sample.personalblog;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleEditRequest {
	private String title;
	private String content;
}
