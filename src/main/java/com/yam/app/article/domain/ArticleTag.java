package com.yam.app.article.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public final class ArticleTag {

    private Long id;
    private Long articleId;
    private Tag tag;

    public ArticleTag(Long articleId, Tag tag) {
        this.articleId = articleId;
        this.tag = tag;
    }

    void setId(Long id) {
        this.id = id;
    }
}
