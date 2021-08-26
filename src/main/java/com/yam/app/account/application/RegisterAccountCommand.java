package com.yam.app.account.application;

import lombok.Getter;

@Getter
public final class RegisterAccountCommand {

    private final String email;
    private final String nickname;
    private final String password;

    public RegisterAccountCommand(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
