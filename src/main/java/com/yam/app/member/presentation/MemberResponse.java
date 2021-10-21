package com.yam.app.member.presentation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class MemberResponse {

    private final Long id;
    private final String nickname;
    private final String image;
}
