package com.yam.app.article.domain;

import java.util.List;
import java.util.Optional;

public final class WriteArticleProcessor {

    private final ArticleRepository articleRepository;
    private final ArticleReader articleReader;
    private final TagRepository tagRepository;
    private final ArticleTagRepository articleTagRepository;

    public WriteArticleProcessor(ArticleRepository articleRepository,
        ArticleReader articleReader, TagRepository tagRepository,
        ArticleTagRepository articleTagRepository) {
        this.articleRepository = articleRepository;
        this.articleReader = articleReader;
        this.tagRepository = tagRepository;
        this.articleTagRepository = articleTagRepository;
    }

    public void write(Long authorId, String title, String content,
        String image, List<String> rawTags) {
        if (articleReader.findByTitle(title) != null) {
            throw new IllegalStateException("Duplicated title");
        }
        articleRepository.save(Article.write(authorId, title, content, image));
        var newArticle = articleReader.findByTitle(title);

        for (String rawTag : rawTags) {
            Tag tag = Optional.ofNullable(tagRepository.findByName(rawTag))
                .orElseGet(() -> {
                    tagRepository.save(new Tag(rawTag));
                    return tagRepository.findByName(rawTag);
                });
            articleTagRepository.save(new ArticleTag(newArticle.getId(), tag));
        }
    }
}
