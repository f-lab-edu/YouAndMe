package com.yam.app.article.presentation;

import com.yam.app.article.application.ArticleFacade;
import com.yam.app.common.Authentication;
import com.yam.app.common.AuthenticationPrincipal;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class ArticleCommandApi {

    private final ArticleFacade articleFacade;

    public ArticleCommandApi(ArticleFacade articleFacade) {
        this.articleFacade = articleFacade;
    }

    @PostMapping("/api/articles/write")
    public ResponseEntity<Void> writeArticle(
        @RequestBody @Valid WriteArticleCommand command,
        @AuthenticationPrincipal Authentication authentication) {
        articleFacade.write(authentication.getMemberId(), command);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
