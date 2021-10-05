package com.yam.app.article.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class FakeArticleRepository implements ArticleRepository, ArticleReader {

    private final Map<Long, Article> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Article findByTitle(String title) {
        return data.values().stream()
            .filter(a -> a.getTitle().equals(title))
            .findFirst()
            .get();
    }

    @Override
    public void save(Article entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
    }
}
