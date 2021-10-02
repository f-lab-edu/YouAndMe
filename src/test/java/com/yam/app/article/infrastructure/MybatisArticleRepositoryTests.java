package com.yam.app.article.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.yam.app.article.domain.Article;
import com.yam.app.article.domain.ArticleReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
final class MybatisArticleRepositoryTests {

    @Autowired
    private ArticleReader articleReader;

    @Test
    void articleReader_find_by_title() {
        Article article = articleReader.findByTitle("sample-title");

        assertThat(article).isNotNull();
    }

    @Test
    void articleReader_find_by_id() {
        Article article = articleReader.findById(1L).get();

        assertThat(article.getTags().size()).isEqualTo(3);
    }
}
