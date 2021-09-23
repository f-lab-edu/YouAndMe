package com.yam.app.account.domain;

import com.yam.app.common.DuplicateValueException;

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

    public Account register(String email, String password) {
        if (accountReader.existsByEmail(email)) {
            throw new DuplicateValueException(email);
        }

        String encodedPassword = passwordEncrypter.encode(password);

        accountRepository.save(Account.of(email, encodedPassword));
        return accountReader.findByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
    }
}
