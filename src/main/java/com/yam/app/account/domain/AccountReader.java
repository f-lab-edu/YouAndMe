package com.yam.app.account.domain;

public interface AccountReader {

    Account findByEmail(String email);
}
