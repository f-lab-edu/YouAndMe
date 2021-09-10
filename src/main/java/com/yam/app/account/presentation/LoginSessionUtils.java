package com.yam.app.account.presentation;

import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class LoginSessionUtils {

    public static final String LOGIN_ACCOUNT_EMAIL = "LOGIN_ACCOUNT_EMAIL";

    private LoginSessionUtils() {
    }

    public static String getLoginAccountEmail() {
        return (String) getHttpSession().getAttribute(LOGIN_ACCOUNT_EMAIL);
    }

    public static void setLoginAccountEmail(String email) {
        getHttpSession().setAttribute(LOGIN_ACCOUNT_EMAIL, email);
    }

    private static HttpSession getHttpSession() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getSession(true);
    }
}
