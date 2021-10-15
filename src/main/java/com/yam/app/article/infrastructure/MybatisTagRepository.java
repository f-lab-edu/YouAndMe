package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.Tag;
import com.yam.app.article.domain.TagRepository;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisTagRepository implements TagRepository {

    private static final String SAVE_FQCN = "com.yam.app.article.domain.TagRepository.save";

    private final SqlSessionTemplate template;

    public MybatisTagRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public void save(Tag entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were saved than expected. : %s", entity));
        }
    }

    @Override
    public Tag findByName(String name) {
        return template.getMapper(TagRepository.class).findByName(name);
    }
}
