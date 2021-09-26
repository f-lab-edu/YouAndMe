package com.yam.app.account.infrastructure;

import java.util.Optional;
import javax.servlet.http.HttpSession;

public final class SessionManager {

    private static final String LOGIN_ACCOUNT = "LOGIN_ACCOUNT_EMAIL";

    private final HttpSession httpSession;

    public SessionManager(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public void setPrincipal(AccountPrincipal principal) {
        this.httpSession.setAttribute(LOGIN_ACCOUNT, principal);
    }

    public Optional<AccountPrincipal> fetchPrincipal() {
        return Optional.ofNullable((AccountPrincipal) httpSession.getAttribute(LOGIN_ACCOUNT));
    }

    public boolean isExistPrincipal() {
        return httpSession.getAttribute(LOGIN_ACCOUNT) != null;
    }

    public void removePrincipal() {
        this.httpSession.removeAttribute(LOGIN_ACCOUNT);
    }
}
