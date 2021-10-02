package com.yam.app.article.domain;

import static java.time.LocalDateTime.now;

import com.yam.app.common.EntityStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public final class Article {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private EntityStatus status = EntityStatus.ALIVE;
    private List<ArticleTag> tags = new ArrayList<>();

    private Article(Long authorId, String title, String content, String image,
        LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    void setId(Long id) {
        this.id = id;
    }

    public static Article write(Long authorId, String title, String content,
        String image) {
        return new Article(authorId, title, content, image, now(), now());
    }
}
