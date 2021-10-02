package com.yam.app.article.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Tag {

    private Long id;
    private String name;

    public Tag(String name) {
        this.name = name;
    }

    void setId(Long id) {
        this.id = id;
    }
}
