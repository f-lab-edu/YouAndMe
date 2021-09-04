package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@DisplayName("검증 도메인 서비스")
class TokenVerifierTest {

    @Test
    @DisplayName("이메일과 토큰의 값을 입력받고 검증에 성공하여 회원의 이메일 검증 상태를 갱신하고, true를 반환한다.")
    void verify_success(){
        //Arrange
        var accountRepository = new FakeAccountRepository();
        var accountReader = accountRepository;
        var tokenVerifier = new TokenVerifier(accountReader, accountRepository);

        var account = accountRepository.save(
            Account.of("jiwonDev@gmail.com", "jiwon", "password!"));

        //Act
        boolean result = tokenVerifier.verify(account.getEmailCheckToken(),
            account.getEmail());
        var updatedAccount = accountReader.findByEmail(account.getEmail());

        //Assert
        assertThat(result).isTrue();
        assertThat(updatedAccount.isEmailVerified()).isTrue();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일과 토큰의 값이 비어있거나 null인 경우 IllegalArgumentException이 발생한다.")
    void param_is_empty_or_null(String arg) {
        // Arrange
        var accountRepository = new FakeAccountRepository();
        var accountReader = new FakeAccountRepository();
        var tokenVerifier = new TokenVerifier(accountReader, accountRepository);

        // Act & Assert
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tokenVerifier.verify(arg, arg));
    }

    @Test
    @DisplayName("유효하지 않은 이메일의 경우 IllegalStateException이 발생한다.")
    void email_is_not_valid() {
        // Arrange
        var accountRepository = new FakeAccountRepository();
        var accountReader = new FakeAccountRepository();
        var tokenVerifier = new TokenVerifier(accountReader, accountRepository);

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> tokenVerifier.verify("abcdefgh", "abcdefgh@naver.com"));
    }

    @Test
    @DisplayName("유효하지 않은 토큰인 경우 IllegalStateException이 발생한다.")
    void token_is_not_valid() {
        // Arrange
        var accountRepository = new FakeAccountRepository();
        var accountReader = new FakeAccountRepository();
        var tokenVerifier = new TokenVerifier(accountReader, accountRepository);

        accountRepository.save(Account.of("jiwonDev@gmail.com", "jiwon", "password!"));

        // Act & Assert
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> tokenVerifier.verify("asdhji", "jiwondev@naver.com"));
    }
}
