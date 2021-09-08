package com.yam.app.account.application;

import com.yam.app.account.domain.Account;
import com.yam.app.account.presentation.AccountResponse;
import org.springframework.stereotype.Component;

@Component
final class AccountTranslator {

    public AccountResponse toResponse(Account entity) {
        return new AccountResponse(entity.getId(), entity.getEmail(),
            entity.getNickname());
    }
}
