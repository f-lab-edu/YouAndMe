package com.yam.app.account.application;

import com.yam.app.account.domain.Account;
import com.yam.app.account.presentation.AccountResponse;
import com.yam.app.account.presentation.RegisterAccountRequest;
import org.springframework.stereotype.Component;

@Component
final class AccountTranslator {

    public RegisterAccountCommand toCommand(RegisterAccountRequest request) {
        return new RegisterAccountCommand(request.getEmail(), request.getNickname(),
            request.getPassword());
    }

    public AccountResponse toResponse(Account entity) {
        return new AccountResponse(entity.getId(), entity.getEmail(),
            entity.getNickname());
    }
}
