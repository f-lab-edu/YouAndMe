package com.yam.app.article.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class FakeArticleTagRepository implements ArticleTagRepository {

    private final Map<Long, ArticleTag> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public void save(ArticleTag entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
    }
}
