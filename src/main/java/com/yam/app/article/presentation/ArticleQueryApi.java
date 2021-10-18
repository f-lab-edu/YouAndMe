package com.yam.app.article.presentation;

import com.yam.app.article.application.ArticleFacade;
import com.yam.app.common.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class ArticleQueryApi {

    private final ArticleFacade articleFacade;

    public ArticleQueryApi(ArticleFacade articleFacade) {
        this.articleFacade = articleFacade;
    }

    @GetMapping("/api/articles/all")
    public ResponseEntity<?> findAll(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "20") int limit) {
        return ResponseEntity.ok(
            ApiResult.success(articleFacade.findAll(offset, limit)));
    }
}
