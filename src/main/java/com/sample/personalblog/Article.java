package com.sample.personalblog;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Article {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime publishedDate;
}
