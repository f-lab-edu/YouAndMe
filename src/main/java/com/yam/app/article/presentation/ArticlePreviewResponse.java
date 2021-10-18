package com.yam.app.article.presentation;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public final class ArticlePreviewResponse {

    private final Long id;
    private final Long authorId;
    private final String title;
    private final String nickname;
    private final String memberImage;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final String status;

    public ArticlePreviewResponse(Long id, Long authorId, String title,
        String nickname, String memberImage, LocalDateTime createdAt,
        LocalDateTime modifiedAt, String status) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.nickname = nickname;
        this.memberImage = memberImage;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.status = status;
    }
}
