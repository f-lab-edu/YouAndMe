package com.yam.app.account.domain;

import com.yam.app.common.EntityNotFoundException;

public final class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException(String email) {
        super("Account could not be found, (email : %s)", email);
    }
}
