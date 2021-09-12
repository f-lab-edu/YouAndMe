package com.yam.app.account.infrastructure;

import com.yam.app.account.common.SessionConst;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.LoginAccountProcessor;
import com.yam.app.account.domain.PasswordEncrypter;
import javax.servlet.http.HttpSession;

public final class SessionBasedLoginAccountProcessor implements LoginAccountProcessor {

    private final AccountReader accountReader;
    private final PasswordEncrypter passwordEncrypter;
    private final HttpSession httpSession;

    public SessionBasedLoginAccountProcessor(AccountReader accountReader,
        PasswordEncrypter passwordEncrypter, HttpSession httpSession) {
        this.accountReader = accountReader;
        this.passwordEncrypter = passwordEncrypter;
        this.httpSession = httpSession;
    }

    @Override
    public void login(String email, String password) {
        var account = accountReader.findByEmail(email)
            .orElseThrow(IllegalStateException::new);

        if (!account.isEmailVerified()) {
            throw new IllegalStateException();
        }

        if (!passwordEncrypter.matches(password, account.getPassword())) {
            throw new IllegalStateException();
        }

        httpSession.setAttribute(SessionConst.LOGIN_ACCOUNT_PRINCIPAL,
            new LoginAccountPrincipal(email));
    }
}
