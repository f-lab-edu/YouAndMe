package com.yam.app.account.presentation;

import lombok.Getter;

@Getter
public final class AccountResponse {

    private final Long id;
    private final String email;
    private final String nickname;

    public AccountResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
