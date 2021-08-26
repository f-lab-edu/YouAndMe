package com.yam.app.account.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class Account {
    private Long id;
    private String email;
    private String nickname;
    private String password;

    public Account(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }
}
