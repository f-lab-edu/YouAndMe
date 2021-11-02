package com.yam.app.article.application;

import com.yam.app.article.domain.ArticleNotFoundException;
import com.yam.app.article.domain.ArticleReader;
import com.yam.app.article.domain.WriteArticleProcessor;
import com.yam.app.article.presentation.ArticlePreviewResponse;
import com.yam.app.article.presentation.ArticleResponse;
import com.yam.app.article.presentation.TagResponse;
import com.yam.app.article.presentation.WriteArticleCommand;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleFacade {

    private final WriteArticleProcessor writeArticleProcessor;
    private final ArticleReader articleReader;

    public ArticleFacade(WriteArticleProcessor writeArticleProcessor,
        ArticleReader articleReader) {
        this.writeArticleProcessor = writeArticleProcessor;
        this.articleReader = articleReader;
    }

    @Transactional
    public void write(Long memberId, WriteArticleCommand command) {
        writeArticleProcessor.write(memberId,
            command.getTitle(), command.getContent(),
            command.getImage(), command.getTags());
    }

    @Transactional(readOnly = true)
    public List<ArticlePreviewResponse> findAll(Long articleId, int pageSize) {
        return articleReader.findAll(articleId, pageSize)
            .stream()
            .map(dto -> new ArticlePreviewResponse(dto.getId(), dto.getAuthorId(), dto.getTitle(),
                dto.getNickname(), dto.getImage(), dto.getCreatedAt(), dto.getModifiedAt(),
                dto.getStatus())
            ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleResponse findById(Long articleId) {
        var article = articleReader.findById(articleId).orElseThrow(
            () -> new ArticleNotFoundException(articleId));

        return ArticleResponse.builder()
            .id(article.getId())
            .authorId(article.getAuthorId())
            .title(article.getTitle())
            .content(article.getContent())
            .image(article.getImage())
            .createdAt(article.getCreatedAt())
            .modifiedAt(article.getModifiedAt())
            .tags(
                article.getTags().stream()
                    .map(a -> TagResponse.of(a.getTag().getId(), a.getTag().getName()))
                    .collect(Collectors.toList())
            )
            .build();

    }
}
