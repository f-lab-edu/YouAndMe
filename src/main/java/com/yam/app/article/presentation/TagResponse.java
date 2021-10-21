package com.yam.app.article.presentation;

import lombok.Getter;

@Getter
public final class TagResponse {

    private final Long id;
    private final String name;

    private TagResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagResponse of(Long id, String name) {
        return new TagResponse(id, name);
    }

}
