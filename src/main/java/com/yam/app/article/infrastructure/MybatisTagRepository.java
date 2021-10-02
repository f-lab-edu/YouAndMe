package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.Tag;
import com.yam.app.article.domain.TagRepository;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisTagRepository implements TagRepository {

    private final SqlSessionTemplate template;

    private static final String SAVE_FQCN = "com.yam.app.article.domain.TagRepository.save";

    public MybatisTagRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public void save(Tag entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }
    }

    @Override
    public Tag findByName(String name) {
        return template.getMapper(TagRepository.class).findByName(name);
    }
}
