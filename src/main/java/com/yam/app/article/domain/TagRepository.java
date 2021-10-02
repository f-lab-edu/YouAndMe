package com.yam.app.article.domain;

public interface TagRepository {

    void save(Tag entity);

    Tag findByName(String name);
}
