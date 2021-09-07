package com.yam.app.account.application;

import lombok.Getter;

@Getter
public final class ConfirmRegisterAccountCommand {

    private final String token;
    private final String email;

    public ConfirmRegisterAccountCommand(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
