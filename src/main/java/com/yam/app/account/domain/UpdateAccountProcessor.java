package com.yam.app.account.domain;

public final class UpdateAccountProcessor {

    private final AccountReader accountReader;
    private final AccountRepository accountRepository;
    private final PasswordEncrypter passwordEncrypter;

    public UpdateAccountProcessor(AccountReader accountReader,
        AccountRepository accountRepository,
        PasswordEncrypter passwordEncrypter) {
        this.accountReader = accountReader;
        this.accountRepository = accountRepository;
        this.passwordEncrypter = passwordEncrypter;
    }

    public void update(String email, String password) {
        var account = accountReader.findByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));
        account.changePassword(passwordEncrypter.encode(password));
        accountRepository.update(account);
    }
}
