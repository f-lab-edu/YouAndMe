package com.yam.app.account.infrastructure;

import javax.servlet.http.HttpSession;

public final class SessionManager {

    public static final String LOGIN_ACCOUNT = "LOGIN_ACCOUNT_EMAIL";

    private final HttpSession httpSession;

    public SessionManager(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public void setPrincipal(AccountPrincipal principal) {
        this.httpSession.setAttribute(LOGIN_ACCOUNT, principal);
    }

    public AccountPrincipal fetchPrincipal() {
        return (AccountPrincipal) httpSession.getAttribute(LOGIN_ACCOUNT);
    }
}
