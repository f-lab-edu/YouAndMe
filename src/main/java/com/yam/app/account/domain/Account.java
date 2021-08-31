package com.yam.app.account.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "password")
public final class Account {

    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String emailCheckToken;
    private LocalDateTime emailCheckTokenGeneratedAt;
    private boolean emailVerified = false;
    private LocalDateTime joinedAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime withdrawalAt;
    private boolean withdraw = false;

    private Account(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static Account of(String email, String nickname, String password) {
        Account account = new Account(email, nickname, password);
        account.generateEmailCheckToken();
        return account;
    }

    void setId(Long id) {
        this.id = id;
    }

    private void generateEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.emailCheckTokenGeneratedAt = LocalDateTime.now();
    }

    public void completeRegister() {
        this.emailVerified = true;
        this.joinedAt = LocalDateTime.now();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }
}
