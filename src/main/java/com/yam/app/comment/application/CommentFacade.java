package com.yam.app.comment.application;

import com.yam.app.comment.domain.CommentProcessor;
import com.yam.app.comment.presentation.CreateCommentCommand;
import com.yam.app.comment.presentation.UpdateCommentCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentFacade {

    private final CommentProcessor commentProcessor;

    public CommentFacade(CommentProcessor commentProcessor) {
        this.commentProcessor = commentProcessor;
    }

    @Transactional
    public Long create(CreateCommentCommand request, Long memberId) {
        return commentProcessor.create(request.getContent(), request.getArticleId(), memberId);
    }

    @Transactional
    public void update(UpdateCommentCommand request, Long commentId, Long memberId) {
        commentProcessor.update(request.getContent(), commentId, memberId);
    }

    @Transactional
    public void delete(Long commentId, Long memberId) {
        commentProcessor.delete(commentId, memberId);
    }
}
