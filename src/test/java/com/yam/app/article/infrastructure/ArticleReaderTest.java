package com.yam.app.article.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.yam.app.article.domain.ArticleDto;
import com.yam.app.article.domain.ArticleReader;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public final class ArticleReaderTest {

    @Autowired
    ArticleReader articleReader;

    @Test
    void articlePaginationQueryTests() {
        List<Long> idx = articleReader.findAll();
        List<ArticleDto> dtos = articleReader.findAllById(1, 10, idx);

        assertThat(dtos.size()).isEqualTo(10);
    }
}
