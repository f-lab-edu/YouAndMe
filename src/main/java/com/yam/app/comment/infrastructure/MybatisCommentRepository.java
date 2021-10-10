package com.yam.app.comment.infrastructure;

import com.yam.app.comment.domain.Comment;
import com.yam.app.comment.domain.CommentReader;
import com.yam.app.comment.domain.CommentRepository;
import java.util.List;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisCommentRepository implements CommentReader, CommentRepository {

    private static final String SAVE_FQCN = "com.yam.app.comment.domain.CommentRepository.save";
    private static final String UPDATE_FQCN = "com.yam.app.comment.domain.CommentRepository.update";
    private static final String DELETE_FQCN = "com.yam.app.comment.domain.CommentRepository.delete";
    private final SqlSessionTemplate template;

    public MybatisCommentRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public Long save(Comment entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }

        return entity.getId();
    }

    @Override
    public void update(Comment entity) {
        int result = template.update(UPDATE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem updating the object : %s", entity));
        }
    }

    @Override
    public void delete(Comment entity) {
        int result = template.update(DELETE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem soft deleting the object : %s", entity));
        }
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return template.getMapper(CommentReader.class).findById(commentId);
    }

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        return template.getMapper(CommentReader.class).findByArticleId(articleId);
    }

    @Override
    public boolean existsById(Long commentId) {
        return template.getMapper(CommentReader.class).existsById(commentId);
    }

}
