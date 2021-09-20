package com.yam.app.account.domain;

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

    public void registerConfirm(String token, String email) {
        tokenVerifier.verify(token, email);
        var account = accountReader.findByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
        account.completeRegister();
        accountRepository.update(account);
    }
}
