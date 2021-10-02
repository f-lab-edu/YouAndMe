package com.yam.app.article.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class FakeTagRepository implements TagRepository {

    private final Map<Long, Tag> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public void save(Tag entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
    }

    @Override
    public Tag findByName(String name) {
        return data.values().stream()
            .filter(t -> t.getName().equals(name))
            .findFirst()
            .get();
    }
}
