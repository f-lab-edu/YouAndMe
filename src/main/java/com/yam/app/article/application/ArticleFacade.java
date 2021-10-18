package com.yam.app.article.application;

import com.yam.app.article.domain.ArticleReader;
import com.yam.app.article.domain.WriteArticleProcessor;
import com.yam.app.article.presentation.ArticlePreviewResponse;
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
    public List<ArticlePreviewResponse> findAll(int offset, int limit) {
        var idx = articleReader.findAll();
        return articleReader.findAllById(offset, limit, idx)
            .stream()
            .map(dto -> new ArticlePreviewResponse(dto.getId(), dto.getAuthorId(), dto.getTitle(),
                dto.getNickname(), dto.getImage(), dto.getCreatedAt(), dto.getModifiedAt(),
                dto.getStatus())
            ).collect(Collectors.toList());
    }
}
