package com.yam.app.comment.presentation;

import com.yam.app.member.presentation.MemberResponse;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class CommentResponse {

    private final Long id;
    private final String content;
    private final LocalDateTime createAt;
    private final LocalDateTime modifiedAt;
    private final Long articleId;
    private final MemberResponse author;

}
