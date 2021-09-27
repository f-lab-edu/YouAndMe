package com.yam.app.account.domain;

import lombok.Getter;

@Getter
public final class RegisterAccountConfirmEvent {

    private final String email;

    public RegisterAccountConfirmEvent(String email) {
        this.email = email;
    }
}
