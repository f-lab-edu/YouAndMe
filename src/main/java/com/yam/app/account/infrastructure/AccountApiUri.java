package com.yam.app.account.infrastructure;

public final class AccountApiUri {

    public static final String REGISTER = "/api/accounts";
    public static final String EMAIL_CONFIRM = "/api/accounts/authorize";
    public static final String LOGIN = "/api/accounts/login";
    public static final String FIND_INFO = "/api/accounts/me";
    public static final String LOGOUT = "/api/accounts/logout";

    public static final String UNAUTHORIZED_REQUEST = "/api/accounts/error/UnauthorizedRequest";

    private AccountApiUri() {
    }

}
