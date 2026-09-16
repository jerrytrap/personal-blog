package com.example.personalblog.article;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Article {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime publishedDate;
}
