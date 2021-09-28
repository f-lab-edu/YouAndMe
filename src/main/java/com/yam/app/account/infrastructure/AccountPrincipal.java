package com.yam.app.account.infrastructure;

import com.yam.app.account.domain.Account;
import com.yam.app.common.Authentication;

public final class AccountPrincipal implements Authentication {

    private final Account account;

    public AccountPrincipal(Account account) {
        this.account = account;
    }

    @Override
    public String getCredentials() {
        return account.getEmail();
    }

    @Override
    public String getRole() {
        return account.getRole().name();
    }

    @Override
    public Long getMemberId() {
        return account.getMemberId();
    }
}
