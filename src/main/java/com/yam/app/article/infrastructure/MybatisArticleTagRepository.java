package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.ArticleTag;
import com.yam.app.article.domain.ArticleTagRepository;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisArticleTagRepository implements ArticleTagRepository {

    private static final String SAVE_FQCN = "com.yam.app.article.domain.ArticleTagRepository.save";

    private final SqlSessionTemplate template;

    public MybatisArticleTagRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public void save(ArticleTag entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were saved than expected. : %s", entity));
        }
    }
}
