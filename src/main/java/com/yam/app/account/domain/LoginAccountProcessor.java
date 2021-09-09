package com.yam.app.account.domain;

public final class LoginAccountProcessor {

    private final AccountReader accountReader;
    private final PasswordEncrypter passwordEncrypter;

    public LoginAccountProcessor(AccountReader accountReader,
        PasswordEncrypter passwordEncrypter) {
        this.accountReader = accountReader;
        this.passwordEncrypter = passwordEncrypter;
    }

    public void login(String email, String password) {
        var account = accountReader.findByEmail(email)
            .filter(Account::isEmailVerified)
            .orElseThrow(IllegalStateException::new);

        if (!passwordEncrypter.matches(password, account.getPassword())) {
            throw new IllegalStateException();
        }
    }
}
