package com.yam.app.account.presentation;

import javax.servlet.http.HttpSession;

public final class SessionUtils {

    private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";

    private SessionUtils() {
    }

    /**
     * 로그인한 회원 아이디를 세션에서 꺼낸다.
     *
     * @param session HttpSession
     * @return 로그인한 회원의 id 또는 null
     */
    public static String getLoginAccountEmail(HttpSession session) {
        return (String) session.getAttribute(LOGIN_MEMBER_ID);
    }

    public static void setLoginAccountEmail(HttpSession session, String id) {
        session.setAttribute(LOGIN_MEMBER_ID, id);
    }

    public static void clear(HttpSession session) {
        session.invalidate();
    }

    public static void logoutMember(HttpSession session) {
        session.removeAttribute(LOGIN_MEMBER_ID);
    }

}
