package com.yam.app.comment.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public final class FakeCommentRepository implements CommentRepository, CommentReader {

    private final Map<Long, Comment> data = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public Optional<Comment> findById(Long commentId) {
        return data.values().stream()
            .filter(c -> c.getId().equals(commentId))
            .findAny();
    }

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        return data.values().stream()
            .filter(c -> c.getArticleId().equals(articleId))
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long commentId) {
        return data.containsKey(commentId);
    }

    @Override
    public void save(Comment entity) {
        entity.setId(idGenerator.incrementAndGet());
        data.put(entity.getId(), entity);
    }

    @Override
    public void update(Comment entity) {
        var comment = data.get(entity.getId());
        comment.update(entity.getContent());
        data.put(entity.getId(), comment);
    }

    @Override
    public void delete(Comment entity) {
        var comment = data.get(entity.getId());
        comment.delete();
        data.put(entity.getId(), comment);
    }
}
