package com.yam.app.comment.domain;

import com.yam.app.common.EntityNotFoundException;

public final class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(Long id) {
        super("Comment could not be found, It may have been deleted.(id : %s)", id);
    }
}

