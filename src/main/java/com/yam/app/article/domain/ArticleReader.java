package com.yam.app.article.domain;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Param;

public interface ArticleReader {

    Article findByTitle(String title);

    Optional<Article> findById(Long articleId);

    boolean existsById(Long articleId);

    List<ArticleDto> findAll(@Param("articleId") Long articleId,
        @Param("pageSize") int pageSize);
}
