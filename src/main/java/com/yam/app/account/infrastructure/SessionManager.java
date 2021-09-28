package com.yam.app.account.infrastructure;

import javax.servlet.http.HttpSession;

public final class SessionManager {

    private static final String LOGIN_ACCOUNT = "LOGIN_ACCOUNT";

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

    public boolean isExistPrincipal() {
        return httpSession.getAttribute(LOGIN_ACCOUNT) != null;
    }

    public void removePrincipal() {
        this.httpSession.removeAttribute(LOGIN_ACCOUNT);
        this.httpSession.invalidate();
    }
}
