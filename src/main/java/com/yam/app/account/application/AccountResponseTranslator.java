package com.yam.app.account.application;

import com.yam.app.account.domain.Account;
import com.yam.app.account.presentation.RegisterAccountResponse;
import org.springframework.stereotype.Component;

@Component
public final class AccountResponseTranslator {

    public RegisterAccountResponse translate(Account entity) {
        return new RegisterAccountResponse(entity.getId(), entity.getEmail(),
            entity.getNickname());
    }
}
