package com.yam.app.comment.presentation;

import com.yam.app.comment.application.CommentFacade;
import com.yam.app.common.ApiResult;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class CommentQueryApi {

    private final CommentFacade commentFacade;

    public CommentQueryApi(CommentFacade commentFacade) {
        this.commentFacade = commentFacade;
    }

    @GetMapping("/api/comments/{articleId}")
    public ResponseEntity<ApiResult<List<CommentResponse>>> getComments(
        @PathVariable Long articleId) {
        return ResponseEntity.ok(
            ApiResult.success(commentFacade.findByArticleId(articleId)));
    }
}
