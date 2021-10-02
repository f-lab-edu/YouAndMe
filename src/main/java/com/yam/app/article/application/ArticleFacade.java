package com.yam.app.article.application;

import com.yam.app.article.domain.WriteArticleProcessor;
import com.yam.app.article.presentation.WriteArticleCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleFacade {

    private final WriteArticleProcessor writeArticleProcessor;

    public ArticleFacade(WriteArticleProcessor writeArticleProcessor) {
        this.writeArticleProcessor = writeArticleProcessor;
    }

    @Transactional
    public void write(Long memberId, WriteArticleCommand command) {
        writeArticleProcessor.write(memberId,
            command.getTitle(), command.getContent(),
            command.getImage(), command.getTags());
    }
}
