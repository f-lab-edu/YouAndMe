package com.yam.app.account.presentation;

import lombok.Getter;

@Getter
public final class AccountResponse {

    private final Long id;
    private final String email;
    private final String nickname;
    private final String image;

    public AccountResponse(Long id, String email, String nickname, String image) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.image = image;
    }
}
