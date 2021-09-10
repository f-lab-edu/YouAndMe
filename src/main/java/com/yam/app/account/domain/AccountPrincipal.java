package com.yam.app.account.domain;

public final class AccountPrincipal {

    private final AccountReader accountReader;

    public AccountPrincipal(AccountReader accountReader) {
        this.accountReader = accountReader;
    }

    public Account getAccount(String email) {
        return accountReader.findByEmail(email)
            .orElseThrow(IllegalStateException::new);
    }

}
