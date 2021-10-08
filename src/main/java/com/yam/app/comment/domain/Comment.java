package com.yam.app.comment.domain;

import static java.time.LocalDateTime.now;

import com.yam.app.common.EntityStatus;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public final class Comment {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private EntityStatus status = EntityStatus.ALIVE;
    private Long articleId;
    private Long memberId;

    private Comment(String content, LocalDateTime createdAt, LocalDateTime modifiedAt,
        Long articleId, Long memberId) {
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.articleId = articleId;
        this.memberId = memberId;
    }

    public static Comment of(String content, Long articleId, Long memberId) {
        return new Comment(content, now(), now(), articleId, memberId);
    }

    public void update(String content) {
        this.content = content;
        this.modifiedAt = now();
    }

    public void delete() {
        this.status = EntityStatus.DELETED;
    }

    public boolean isAlive() {
        return this.status == EntityStatus.ALIVE;
    }

    public boolean isAuthor(Long memberId) {
        return this.memberId.equals(memberId);
    }

    void setId(Long id) {
        this.id = id;
    }
}
