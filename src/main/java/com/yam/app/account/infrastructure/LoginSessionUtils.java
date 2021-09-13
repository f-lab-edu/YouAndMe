package com.yam.app.account.infrastructure;

import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LoginSessionUtils {

    public static final String LOGIN_ACCOUNT_EMAIL = "LOGIN_ACCOUNT_EMAIL";

    private LoginSessionUtils() {
    }

    private static HttpSession getHttpSession() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getSession(true);
    }

    public static AccountPrincipal getAccountPrincipal() {
        return (AccountPrincipal) getHttpSession().getAttribute(LOGIN_ACCOUNT_EMAIL);
    }

    public static void setAccountPrincipal(AccountPrincipal accountPrincipal) {
        getHttpSession().setAttribute(LOGIN_ACCOUNT_EMAIL, accountPrincipal);
    }
}
