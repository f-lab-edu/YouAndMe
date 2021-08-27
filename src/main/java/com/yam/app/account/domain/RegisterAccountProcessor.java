package com.yam.app.account.domain;

public final class RegisterAccountProcessor {

    private final AccountRepository accountRepository;
    private final PasswordEncrypter passwordEncrypter;

    public RegisterAccountProcessor(AccountRepository accountRepository,
        PasswordEncrypter passwordEncrypter) {
        this.accountRepository = accountRepository;
        this.passwordEncrypter = passwordEncrypter;
    }

    public Account process(String email, String nickname, String password) {
        if (accountRepository.existsByEmail(email)) {
            throw new IllegalStateException();
        }
        if (accountRepository.existsByNickname(nickname)) {
            throw new IllegalStateException();
        }

        String encodedPassword = passwordEncrypter.encode(password);

        return accountRepository.save(new Account(email, nickname, encodedPassword));
    }
}
