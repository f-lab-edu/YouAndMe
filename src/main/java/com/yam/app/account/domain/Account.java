package com.yam.app.account.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "password")
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

    public void setId(Long id) {
        this.id = id;
    }
}
