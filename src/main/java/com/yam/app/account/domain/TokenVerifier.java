package com.yam.app.account.domain;

import com.yam.app.common.StringUtils;

public final class TokenVerifier {

    private final AccountReader accountReader;
    private final AccountRepository accountRepository;

    public TokenVerifier(AccountReader accountReader,
        AccountRepository accountRepository) {
        this.accountReader = accountReader;
        this.accountRepository = accountRepository;
    }

    public boolean verify(String token, String email) {
        if (StringUtils.isBlank(token) || StringUtils.isBlank(email)) {
            throw new IllegalArgumentException();
        }

        var account = accountReader.findByEmail(email);

        if (account == null) {
            throw new IllegalStateException();
        }

        if (!account.isValidToken(token)) {
            throw new IllegalStateException();
        }

        account.completeRegister();
        accountRepository.update(account);
        return true;
    }

}
