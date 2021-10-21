package com.yam.app.article.presentation;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class ArticleResponse {

    private final Long id;
    private final Long authorId;
    private final String title;
    private final String content;
    private final String image;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    private final List<TagResponse> tags;

    private ArticleResponse(Long id, Long authorId, String title, String content, String image,
        LocalDateTime createdAt, LocalDateTime modifiedAt,
        List<TagResponse> tags) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.tags = tags;
    }

}
