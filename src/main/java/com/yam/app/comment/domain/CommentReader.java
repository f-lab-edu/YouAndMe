package com.yam.app.comment.domain;

import java.util.List;
import java.util.Optional;

public interface CommentReader {

    Optional<Comment> findById(Long commentId);

    List<Comment> findByArticleId(Long articleId);

    boolean existsById(Long commentId);
}
