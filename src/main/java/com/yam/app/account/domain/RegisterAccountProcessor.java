package com.yam.app.account.domain;

public final class RegisterAccountProcessor {

    private final AccountRepository accountRepository;

    public RegisterAccountProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account process(String email, String nickname, String password) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalStateException();
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw new IllegalStateException();
        }

        return accountRepository.save(new Account(email, nickname, password));
    }
}
