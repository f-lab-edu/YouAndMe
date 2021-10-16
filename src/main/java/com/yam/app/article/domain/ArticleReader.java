package com.yam.app.article.domain;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

public interface ArticleReader {

    Article findByTitle(String title);

    Optional<Article> findById(Long articleId);

    boolean existsById(Long articleId);

    List<Long> findAll(@Param("offset") int offset, @Param("limit") int limit);
}
