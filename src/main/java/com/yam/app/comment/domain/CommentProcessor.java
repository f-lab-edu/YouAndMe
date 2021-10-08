package com.yam.app.comment.domain;

import com.yam.app.article.domain.ArticleReader;
import com.yam.app.common.UnauthorizedRequestException;

public final class CommentProcessor {

    private final CommentReader commentReader;
    private final CommentRepository commentRepository;
    private final ArticleReader articleReader;

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

    public void update(String content, Long commentId, Long memberId) {
        var comment = this.getComment(commentId, memberId);

        if (!comment.isAlive()) {
            throw new CommentNotFoundException(commentId);
        }

        comment.update(content);
        commentRepository.update(comment);
    }

    public void delete(Long commentId, Long memberId) {
        var comment = this.getComment(commentId, memberId);

        comment.delete();
        commentRepository.delete(comment);
    }

    private Comment getComment(Long commentId, Long memberId) {
        var comment = commentReader.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!articleReader.existsById(comment.getArticleId())) {
            throw new ArticleNotFoundException(comment.getArticleId());
        }

        if (!comment.isAuthor(memberId)) {
            throw new UnauthorizedRequestException(
                "Member is not the author of this comment, id: " + memberId);
        }

        return comment;
    }

}
