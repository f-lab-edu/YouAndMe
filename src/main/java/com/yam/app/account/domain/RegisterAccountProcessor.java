package com.yam.app.account.domain;

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

    public Account process(String email, String nickname, String password) {
        if (accountReader.existsByEmail(email)) {
            throw new IllegalStateException();
        }
        if (accountReader.existsByNickname(nickname)) {
            throw new IllegalStateException();
        }

        String encodedPassword = passwordEncrypter.encode(password);

        accountRepository.save(Account.of(email, nickname, encodedPassword));
        return accountReader.findByEmail(email)
            .orElseThrow(IllegalArgumentException::new);
    }
}
