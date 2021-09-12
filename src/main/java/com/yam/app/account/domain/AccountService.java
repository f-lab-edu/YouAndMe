package com.yam.app.account.domain;

public final class AccountService {

    private final AccountReader accountReader;

    public AccountService(AccountReader accountReader) {
        this.accountReader = accountReader;
    }

    public Account findByEmail(String email) {
        return accountReader.findByEmail(email)
            .orElseThrow(IllegalArgumentException::new);
    }

}
