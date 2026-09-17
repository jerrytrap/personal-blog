package com.example.personalblog.article;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

@Primary
@Repository
public class FileArticleRepository implements ArticleRepository {
	private final ObjectMapper objectMapper;
	private final Path storageDir = Paths.get("data/articles");

	public FileArticleRepository(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;

		try {
			Files.createDirectories(storageDir);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public List<Article> findAll() {
		try (Stream<Path> files = Files.list(storageDir)) {
			List<Article> result = new ArrayList<>();

			for (Path path : files.filter(file -> file.toString().endsWith(".json")).toList()) {
				result.add(readArticle(path));
			}

			result.sort(Comparator.comparing(Article::getPublishedDate));
			return result;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public Optional<Article> findById(UUID id) {
		Path path = getPath(id);

		if (!Files.exists(path)) {
			return Optional.empty();
		}

		return Optional.of(readArticle(path));
	}

	@Override
	public void save(Article article) {
		try {
			Path path = getPath(article.getId());

			objectMapper.writerWithDefaultPrettyPrinter()
				.writeValue(path.toFile(), article);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	@Override
	public void deleteById(UUID id) {
		try {
			Files.deleteIfExists(getPath(id));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private Path getPath(UUID id) {
		return storageDir.resolve(id + ".json");
	}

	private Article readArticle(Path path) {
		try {
			return objectMapper.readValue(path.toFile(), Article.class);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
