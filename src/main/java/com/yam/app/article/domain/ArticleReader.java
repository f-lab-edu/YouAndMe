package com.yam.app.article.domain;

import java.util.Optional;

public interface ArticleReader {

    Article findByTitle(String title);

    Optional<Article> findById(Long articleId);

    boolean existsById(Long articleId);
}
