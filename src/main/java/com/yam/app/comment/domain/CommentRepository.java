package com.yam.app.comment.domain;

public interface CommentRepository {

    void save(Comment entity);

    void update(Comment entity);

    void delete(Comment entity);

}
