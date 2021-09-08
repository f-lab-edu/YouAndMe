package com.yam.app.account.domain;

import com.yam.app.account.application.LoginAccountCommand;

public final class LoginAccountProcessor {

    private final AccountReader accountReader;
    private final PasswordEncrypter passwordEncrypter;

    public LoginAccountProcessor(AccountReader accountReader,
        PasswordEncrypter passwordEncrypter) {
        this.accountReader = accountReader;
        this.passwordEncrypter = passwordEncrypter;
    }

    public void login(LoginAccountCommand toCommand) {
        var account = accountReader.findByEmail(toCommand.getEmail())
            .orElseThrow(IllegalStateException::new);

        if (!passwordEncrypter.matches(toCommand.getPassword(), account.getPassword())) {
            throw new IllegalStateException();
        }
    }
}
