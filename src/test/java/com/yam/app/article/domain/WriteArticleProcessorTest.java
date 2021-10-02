package com.yam.app.article.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("게시글 작성 도메인 서비스")
class WriteArticleProcessorTest {

    @Test
    @DisplayName("게시글 제목이 중복되어 게시글 작성 실패.")
    void should_write_article_fail_duplicated_title() {
        // Arrange
        final var fakeObject = new FakeArticleRepository();
        final var articleReader = fakeObject;
        final var articleRepository = fakeObject;
        final var tagRepository = new FakeTagRepository();
        final var articleTagRepository = new FakeArticleTagRepository();
        articleRepository.save(Article.write(1L, "duplicate", "sample", "image.png"));
        var sut = new WriteArticleProcessor(articleRepository, articleReader,
            tagRepository, articleTagRepository);

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> sut.write(1L, "duplicate", "sample",
                "image.png", Collections.emptyList()));
    }
}
