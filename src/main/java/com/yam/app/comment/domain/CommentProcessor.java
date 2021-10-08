package com.yam.app.comment.domain;

import com.yam.app.article.domain.ArticleReader;

public final class CommentProcessor {

    CommentReader commentReader;
    CommentRepository commentRepository;
    ArticleReader articleReader;

    public CommentProcessor(CommentReader commentReader,
        CommentRepository commentRepository, ArticleReader articleReader) {
        this.commentReader = commentReader;
        this.commentRepository = commentRepository;
        this.articleReader = articleReader;
    }

    public void create(String content, Long articleId, Long memberId) {
        if (!articleReader.existsById(articleId)) {
            throw new ArticleNotFoundException(articleId);
        }

        commentRepository.save(Comment.of(content, articleId, memberId));
    }

    public void update(String content, Long commentId) {
        Comment comment = commentReader.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!articleReader.existsById(comment.getArticleId())) {
            throw new ArticleNotFoundException(comment.getArticleId());
        }

        if (!comment.isAlive()) {
            throw new CommentNotFoundException(commentId);
        }

        comment.update(content);
        commentRepository.update(comment);
    }

    public void delete(Long commentId) {
        Comment comment = commentReader.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!articleReader.existsById(comment.getArticleId())) {
            throw new ArticleNotFoundException(comment.getArticleId());
        }

        comment.delete();
        commentRepository.delete(comment);
    }

}
