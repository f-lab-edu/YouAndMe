package com.yam.app.account.infrastructure;

import com.yam.app.common.Authentication;
import java.util.Optional;
import javax.servlet.http.HttpSession;

public final class SessionManager {

    private static final String LOGIN_ACCOUNT = "LOGIN_ACCOUNT";

    private final HttpSession httpSession;

    public SessionManager(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public void setPrincipal(Authentication principal) {
        this.httpSession.setAttribute(LOGIN_ACCOUNT, principal);
    }

    public Optional<Authentication> fetchPrincipal() {
        return Optional.ofNullable((Authentication) httpSession.getAttribute(LOGIN_ACCOUNT));
    }

    public void removePrincipal() {
        this.httpSession.removeAttribute(LOGIN_ACCOUNT);
        this.httpSession.invalidate();
    }
}
