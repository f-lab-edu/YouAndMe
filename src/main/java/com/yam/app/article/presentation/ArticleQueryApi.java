package com.yam.app.article.presentation;

import com.yam.app.article.application.ArticleFacade;
import com.yam.app.common.ApiResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        @RequestParam(value = "articleId", defaultValue = "0") Long articleId,
        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(
            ApiResult.success(articleFacade.findAll(articleId, pageSize)));
    }

    @GetMapping("/api/articles/{articleId}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long articleId) {
        return ResponseEntity.ok(articleFacade.findById(articleId));
    }
}
