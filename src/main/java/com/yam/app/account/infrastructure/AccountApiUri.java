package com.yam.app.account.presentation;

public final class AccountApiUri {

    String REGISTER = "/api/accounts";
    String EMAIL_CONFIRM = "/api/accounts/authorize";
    String LOGIN = "/api/accounts/login";
    String FIND_INFO = "/api/accounts/me";

    String UNAUTHORIZED_REQUEST = "/api/error/UnauthorizedRequest";

    private AccountApiUri() {
    }

}
