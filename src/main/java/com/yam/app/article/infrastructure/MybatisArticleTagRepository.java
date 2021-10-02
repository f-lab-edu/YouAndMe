package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.ArticleTag;
import com.yam.app.article.domain.ArticleTagRepository;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisArticleTagRepository implements ArticleTagRepository {

    private final SqlSessionTemplate template;

    private static final String SAVE_FQCN = "com.yam.app.article.domain.ArticleTagRepository.save";

    public MybatisArticleTagRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public void save(ArticleTag entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }
    }
}
