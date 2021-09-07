package com.yam.app.account.domain;

import com.yam.app.account.application.LoginAccountCommand;

public final class LoginAccountProcessor {

    private final AccountReader accountReader;

    public LoginAccountProcessor(AccountReader accountReader) {
        this.accountReader = accountReader;
    }

    /**
     * 아직 구현되지 않은 메서드.
     */
    public void login(LoginAccountCommand toCommand) {
    }
}
