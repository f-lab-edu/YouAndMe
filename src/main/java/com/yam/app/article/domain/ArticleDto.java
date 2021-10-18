package com.yam.app.article.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public final class ArticleDto {

    private Long id;
    private Long authorId;
    private String title;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;
    private String image;
}
