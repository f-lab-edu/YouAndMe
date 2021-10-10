package com.yam.app.comment.application;

import com.yam.app.comment.domain.CommentProcessor;
import com.yam.app.comment.presentation.CreateCommentCommand;
import com.yam.app.comment.presentation.UpdateCommentCommand;
import org.springframework.stereotype.Service;

@Service
public class CommentFacade {

    private final CommentProcessor commentProcessor;

    public CommentFacade(CommentProcessor commentProcessor) {
        this.commentProcessor = commentProcessor;
    }

    public Long create(CreateCommentCommand request, Long memberId) {
        return commentProcessor.create(request.getContent(), request.getArticleId(), memberId);
    }

    public void update(UpdateCommentCommand request, Long commentId, Long memberId) {
        commentProcessor.update(request.getContent(), commentId, memberId);
    }
}
