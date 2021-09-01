package com.yam.app.account.domain;

import com.yam.app.account.application.RegisterAccountCommand;

public final class RegisterAccountProcessor {

    private final AccountRepository accountRepository;
    private final PasswordEncrypter passwordEncrypter;

    public RegisterAccountProcessor(AccountRepository accountRepository,
        PasswordEncrypter passwordEncrypter) {
        this.accountRepository = accountRepository;
        this.passwordEncrypter = passwordEncrypter;
    }

    public Account process(RegisterAccountCommand command) {
        if (accountRepository.existsByEmail(command.getEmail())) {
            throw new IllegalStateException();
        }
        if (accountRepository.existsByNickname(command.getNickname())) {
            throw new IllegalStateException();
        }

        String encodedPassword = passwordEncrypter.encode(command.getPassword());

        return accountRepository.save(
            Account.of(command.getEmail(), command.getNickname(), encodedPassword));
    }
}
