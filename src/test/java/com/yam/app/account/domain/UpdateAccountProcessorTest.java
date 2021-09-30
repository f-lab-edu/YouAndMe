package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("계정 정보 수정 도메인 서비스")
class UpdateAccountProcessorTest {

    @Test
    @DisplayName("계정을 가져오는데 실패하여 예외를 발생한다.")
    void should_update_fail_find_by_email_error() {
        // Arrange
        final var fakeObject = new FakeAccountRepository();
        final var accountRepository = fakeObject;
        final var accountReader = fakeObject;
        final var passwordEncrypter = new PasswordEncrypterStub();
        var sut = new UpdateAccountProcessor(accountReader,
            accountRepository, passwordEncrypter);

        // Act & Assert
        assertThatExceptionOfType(AccountNotFoundException.class)
            .isThrownBy(() -> sut.update("wrong@gmail.com", "password!"));
    }
}
