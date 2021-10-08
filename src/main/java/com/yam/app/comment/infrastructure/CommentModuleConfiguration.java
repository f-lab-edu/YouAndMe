package com.yam.app.comment.infrastructure;

import com.yam.app.article.domain.ArticleReader;
import com.yam.app.comment.domain.CommentProcessor;
import com.yam.app.comment.domain.CommentReader;
import com.yam.app.comment.domain.CommentRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommentModuleConfiguration {

    @Bean
    public CommentReader commentReader(SqlSessionTemplate template) {
        return new MybatisCommentRepository(template);
    }

    @Bean
    public CommentRepository commentRepository(SqlSessionTemplate template) {
        return new MybatisCommentRepository(template);
    }

    @Bean
    public CommentProcessor commentProcessor(CommentReader commentReader,
        CommentRepository commentRepository, ArticleReader articleReader) {
        return new CommentProcessor(commentReader, commentRepository, articleReader);
    }
}
