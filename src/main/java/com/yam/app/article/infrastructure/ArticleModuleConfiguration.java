package com.yam.app.article.infrastructure;

import com.yam.app.article.domain.ArticleReader;
import com.yam.app.article.domain.ArticleRepository;
import com.yam.app.article.domain.ArticleTagRepository;
import com.yam.app.article.domain.TagRepository;
import com.yam.app.article.domain.WriteArticleProcessor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArticleModuleConfiguration {

    @Bean
    public ArticleReader articleReader(SqlSessionTemplate template) {
        return new MybatisArticleRepository(template);
    }

    @Bean
    public ArticleRepository articleRepository(SqlSessionTemplate template) {
        return new MybatisArticleRepository(template);
    }

    @Bean
    public ArticleTagRepository articleTagRepository(SqlSessionTemplate template) {
        return new MybatisArticleTagRepository(template);
    }

    @Bean
    public TagRepository tagRepository(SqlSessionTemplate template) {
        return new MybatisTagRepository(template);
    }

    @Bean
    public WriteArticleProcessor writeArticleProcessor(
        TagRepository tagRepository,
        ArticleRepository articleRepository,
        ArticleReader articleReader,
        ArticleTagRepository articleTagRepository
    ) {
        return new WriteArticleProcessor(articleRepository, articleReader, tagRepository,
            articleTagRepository);
    }
}
