package com.yam.app.account.domain;

public final class TokenVerifier {

    private final AccountReader accountReader;

    public TokenVerifier(AccountReader accountReader) {
        this.accountReader = accountReader;
    }

    public void verify(String token, String email) {
        var account = accountReader.findByEmail(email)
            .orElseThrow(() -> new AccountNotFoundException(email));

        if (!account.isValidToken(token)) {
            throw new IllegalStateException("Invalid token");
        }
    }
}
