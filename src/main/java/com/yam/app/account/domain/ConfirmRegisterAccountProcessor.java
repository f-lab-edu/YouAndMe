package com.yam.app.account.domain;

import com.yam.app.account.application.ConfirmRegisterAccountCommand;

public final class ConfirmRegisterAccountProcessor {

    private final AccountReader accountReader;
    private final AccountRepository accountRepository;
    private final TokenVerifier tokenVerifier;

    public ConfirmRegisterAccountProcessor(AccountReader accountReader,
        AccountRepository accountRepository, TokenVerifier tokenVerifier) {
        this.accountReader = accountReader;
        this.accountRepository = accountRepository;
        this.tokenVerifier = tokenVerifier;
    }

    public void registerConfirm(ConfirmRegisterAccountCommand command) {
        tokenVerifier.verify(command.getToken(), command.getEmail());
        var account = accountReader.findByEmail(command.getEmail())
            .orElseThrow(IllegalArgumentException::new);
        account.completeRegister();
        accountRepository.update(account);
    }
}
