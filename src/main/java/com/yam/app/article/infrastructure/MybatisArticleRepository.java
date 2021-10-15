package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.Article;
import com.yam.app.article.domain.ArticleReader;
import com.yam.app.article.domain.ArticleRepository;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisArticleRepository implements ArticleReader, ArticleRepository {

    private static final String SAVE_FQCN = "com.yam.app.article.domain.ArticleRepository.save";

    private final SqlSessionTemplate template;

    public MybatisArticleRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public void save(Article entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were saved than expected. : %s", entity));
        }
    }

    @Override
    public Article findByTitle(String title) {
        return template.getMapper(ArticleReader.class).findByTitle(title);
    }

    @Override
    public Optional<Article> findById(Long articleId) {
        return template.getMapper(ArticleReader.class).findById(articleId);
    }

    @Override
    public boolean existsById(Long articleId) {
        return template.getMapper(ArticleReader.class).existsById(articleId);
    }

}
