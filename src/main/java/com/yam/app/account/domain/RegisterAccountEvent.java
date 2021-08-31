package com.yam.app.account.domain;

import lombok.Getter;

@Getter
public final class RegisterAccountEvent {

    private final Account account;

    public RegisterAccountEvent(Account account) {
        this.account = account;
    }
}
