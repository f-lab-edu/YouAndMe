package com.yam.app.comment.application;

import com.yam.app.article.domain.ArticleNotFoundException;
import com.yam.app.article.domain.ArticleReader;
import com.yam.app.comment.domain.Comment;
import com.yam.app.comment.domain.CommentProcessor;
import com.yam.app.comment.domain.CommentReader;
import com.yam.app.comment.presentation.CommentResponse;
import com.yam.app.comment.presentation.CreateCommentCommand;
import com.yam.app.comment.presentation.UpdateCommentCommand;
import com.yam.app.member.domain.MemberReader;
import com.yam.app.member.presentation.MemberResponse;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentFacade {

    private final CommentProcessor commentProcessor;
    private final ArticleReader articleReader;
    private final CommentReader commentReader;
    private final MemberReader memberReader;

    public CommentFacade(CommentProcessor commentProcessor,
        ArticleReader articleReader, CommentReader commentReader,
        MemberReader memberReader) {
        this.commentProcessor = commentProcessor;
        this.articleReader = articleReader;
        this.commentReader = commentReader;
        this.memberReader = memberReader;
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

    @Transactional(readOnly = true)
    public List<CommentResponse> findByArticleId(Long articleId) {
        if (!articleReader.existsById(articleId)) {
            throw new ArticleNotFoundException(articleId);
        }

        return commentReader.findByArticleId(articleId)
            .stream()
            .filter(Comment::isAlive)
            .map(this::toCommentResponse)
            .collect(Collectors.toList());
    }

    private CommentResponse toCommentResponse(Comment comment) {
        var member = memberReader.findById(comment.getMemberId())
            .orElseThrow(IllegalStateException::new);

        return CommentResponse.builder()
            .id(comment.getId())
            .articleId(comment.getArticleId())
            .content(comment.getContent())
            .createAt(comment.getCreatedAt())
            .modifiedAt(comment.getModifiedAt())
            .author(
                MemberResponse.builder()
                    .id(member.getId())
                    .image(member.getImage())
                    .nickname(member.getNickname())
                    .build())
            .build();
    }
}
