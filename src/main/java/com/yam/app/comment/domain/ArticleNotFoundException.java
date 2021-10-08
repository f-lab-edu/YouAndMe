package com.yam.app.comment.domain;

import com.yam.app.common.EntityNotFoundException;

public final class ArticleNotFoundException extends EntityNotFoundException {

    public ArticleNotFoundException(Long id) {
        super("Article could not be found, (id : %s)", id);
    }
}

