package com.yam.app.account.domain;

import com.yam.app.account.application.RegisterAccountCommand;

public final class RegisterAccountProcessor {

    private final AccountRepository accountRepository;
    private final AccountReader accountReader;
    private final PasswordEncrypter passwordEncrypter;

    public RegisterAccountProcessor(AccountRepository accountRepository,
        AccountReader accountReader, PasswordEncrypter passwordEncrypter) {
        this.accountRepository = accountRepository;
        this.accountReader = accountReader;
        this.passwordEncrypter = passwordEncrypter;
    }

    public Account process(RegisterAccountCommand command) {
        if (accountReader.existsByEmail(command.getEmail())) {
            throw new IllegalStateException();
        }
        if (accountReader.existsByNickname(command.getNickname())) {
            throw new IllegalStateException();
        }

        String encodedPassword = passwordEncrypter.encode(command.getPassword());

        return accountRepository.save(
            Account.of(command.getEmail(), command.getNickname(), encodedPassword));
    }
}
