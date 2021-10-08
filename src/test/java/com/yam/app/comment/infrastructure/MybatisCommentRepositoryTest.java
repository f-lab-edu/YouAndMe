package com.yam.app.comment.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.yam.app.comment.domain.CommentReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
final class MybatisCommentRepositoryTest {

    @Autowired
    CommentReader commentReader;

    @Test
    void findById() {
        var comment = commentReader.findById(1L);
        if (comment.isPresent()) {
            assertThat(comment.get()).isNotNull();
        } else {
            fail("Comment Could not find");
        }

    }

    @Test
    void findByArticleId() {
        var comments = commentReader.findByArticleId(1L);
        assertThat(comments).extracting("articleId", Long.class)
            .containsOnly(1L);
    }

    @Test
    void existsById() {
        var isPresent = commentReader.existsById(1L);
        assertThat(isPresent).isTrue();
    }
}
