package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.ArticleReader;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public final class ArticleReaderTest {

    @Autowired
    ArticleReader articleReader;

    @Test
    void name() {
        List<Long> idx = articleReader.findAll(0, 10);
    }
}
