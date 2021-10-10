package com.yam.app.comment.presentation;

import com.yam.app.comment.application.CommentFacade;
import com.yam.app.common.Authentication;
import com.yam.app.common.AuthenticationPrincipal;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class CommentCommandApi {

    private final CommentFacade commentFacade;

    public CommentCommandApi(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    @PostMapping("/api/comments/create")
    public ResponseEntity<Void> createComment(
        @RequestBody @Valid CreateCommentCommand request,
        @AuthenticationPrincipal Authentication authentication) {

        final Long commentId = commentFacade.create(request, authentication.getMemberId());

        return ResponseEntity
            .created(URI.create("/comments/" + commentId))
            .build();
    }

    @PatchMapping("api/comments/{commentId}")
    public ResponseEntity<Void> updateComment(
        @PathVariable Long commentId,
        @RequestBody @Valid UpdateCommentCommand request,
        @AuthenticationPrincipal Authentication authentication) {

        commentFacade.update(request, commentId, authentication.getMemberId());

        return ResponseEntity.ok().build();
    }

}

