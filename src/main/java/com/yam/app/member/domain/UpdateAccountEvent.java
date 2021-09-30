package com.yam.app.member.domain;

import lombok.Getter;

@Getter
public final class UpdateAccountEvent {

    private final Long memberId;
    private final String nickname;
    private final String image;

    public UpdateAccountEvent(Long memberId, String nickname, String image) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.image = image;
    }
}
