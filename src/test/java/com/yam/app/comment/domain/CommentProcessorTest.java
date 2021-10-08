package com.yam.app.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.yam.app.article.domain.Article;
import com.yam.app.article.domain.FakeArticleRepository;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("댓글 작성 도메인 서비스")
final class CommentProcessorTest {

    @TestFactory
    @DisplayName("댓글 시나리오")
    Collection<DynamicTest> comment_scenarios() {
        final var fakeCommentRepository = new FakeCommentRepository();
        final var fakeArticleRepository = new FakeArticleRepository();
        final var processor = new CommentProcessor(fakeCommentRepository,
            fakeCommentRepository, fakeArticleRepository);

        final var memberId = 1L;
        final var articleId = 1L;
        final var commentId = 1L;
        final var deletedCommentId = 2L;
        final var wrongCommentId = 3L;

        fakeArticleRepository.save(Article.write(1L, "title", "content", "image.png"));
        fakeArticleRepository.save(Article.write(2L, "title", "content", "image.png"));
        fakeCommentRepository.save(Comment.of("sample content", articleId, memberId));
        fakeCommentRepository.save(Comment.of("deleted content", articleId, memberId));
        fakeCommentRepository.save(Comment.of("article not found", 4321L, memberId));

        return Arrays.asList(
            DynamicTest.dynamicTest("댓글 작성에 성공한다.",
                () -> {
                    // Act
                    processor.create("hello", 2L, memberId);
                    var comments = fakeCommentRepository.findByArticleId(2L);

                    // Assert
                    assertThat(comments.get(0).getId()).isEqualTo(4L);
                }),
            DynamicTest.dynamicTest("댓글 수정에 성공한다.",
                () -> {
                    // Act
                    var modifiedAt = fakeCommentRepository.findById(commentId).get()
                        .getModifiedAt();

                    processor.update("new content", commentId);
                    var comment = fakeCommentRepository.findById(commentId).get();

                    // Assert
                    assertThat(comment.getContent()).isEqualTo("new content");
                    assertThat(comment.getModifiedAt().isAfter(modifiedAt)).isTrue();
                }),
            DynamicTest.dynamicTest("댓글 작성시 유효한 게시글이 존재하지 않는 경우 예외를 반환한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(ArticleNotFoundException.class)
                        .isThrownBy(() -> processor.create("sample content", 1234L, memberId));
                }),
            DynamicTest.dynamicTest("댓글 수정시 유효한 게시글이 존재하지 않는 경우 예외를 반환한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(ArticleNotFoundException.class)
                        .isThrownBy(() -> processor.update("new content", wrongCommentId));
                }),
            DynamicTest.dynamicTest("댓글 삭제시 유효한 게시글이 존재하지 않는 경우 예외를 반환한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(ArticleNotFoundException.class)
                        .isThrownBy(() -> processor.delete(wrongCommentId));
                }),

            DynamicTest.dynamicTest("댓글 수정시 유효한 댓글이 존재하지 않는 경우 예외를 반환한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(CommentNotFoundException.class)
                        .isThrownBy(() -> processor.update("new content", 1234L));
                }),
            DynamicTest.dynamicTest("삭제된 댓글을 수정하려고 하는 경우 예외를 반환한다.",
                () -> {
                    // Act & Assert
                    processor.delete(deletedCommentId);
                    assertThatExceptionOfType(CommentNotFoundException.class)
                        .isThrownBy(
                            () -> processor.update("sample content", deletedCommentId));
                })
        );
    }
}
