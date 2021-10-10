package com.yam.app.comment.presentation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public final class CreateCommentCommand {

    @NotNull
    private Long articleId;

    @Length(max = 120)
    @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
    private String content;
}
