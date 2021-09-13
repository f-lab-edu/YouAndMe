package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.PasswordEncrypter;

public final class SessionBasedLoginAccountProcessor implements LoginAccountProcessor {

    private final AccountReader accountReader;
    private final PasswordEncrypter passwordEncrypter;

    public SessionBasedLoginAccountProcessor(AccountReader accountReader,
        PasswordEncrypter passwordEncrypter) {
        this.accountReader = accountReader;
        this.passwordEncrypter = passwordEncrypter;
    }

    @Override
    public void login(String email, String password) {
        var account = accountReader.findByEmail(email)
            .orElseThrow(IllegalArgumentException::new);

        if (!account.isEmailVerified()) {
            throw new IllegalStateException();
        }

        if (!passwordEncrypter.matches(password, account.getPassword())) {
            throw new IllegalStateException();
        }

        LoginSessionUtils.setAccountPrincipal(new AccountPrincipal(email));
    }
}
