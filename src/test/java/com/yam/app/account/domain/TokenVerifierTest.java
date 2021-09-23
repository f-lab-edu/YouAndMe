package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("토큰 검증 도메인 테스트")
final class TokenVerifierTest {

    @Test
    @DisplayName("입력된 이메일과 토큰값을 예외를 발생시키지 않고 올바르게 검증하는지 테스트한다.")
    void verify_email_and_token_correctly() {
        //Arrange
        var accountRepository = new FakeAccountRepository();
        var tokenVerifier = new TokenVerifier(accountRepository);
        accountRepository.save(Account.of("jiwon@gmail.com", "password!"));
        var account = accountRepository.findByEmail("jiwon@gmail.com").get();

        //Act
        var throwable = catchThrowable(
            () -> tokenVerifier.verify(account.getEmailCheckToken(), account.getEmail()));

        // Assert
        assertThat(throwable).isNull();
    }
}
