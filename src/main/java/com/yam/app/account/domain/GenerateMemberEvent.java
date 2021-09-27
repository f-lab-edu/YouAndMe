package com.yam.app.account.domain;

import lombok.Getter;

@Getter
public final class GenerateMemberEvent {

    private final Long memberId;
    private final String email;

    public GenerateMemberEvent(Long memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}
