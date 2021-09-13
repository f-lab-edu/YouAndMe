package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class PasswordEncrypterTest {

    @Test
    @DisplayName("비밀번호를 인코딩하고 올바르게 일치하는지 검증한다.")
    void sut_password_encoded_and_matches_correctly() {
        // Arrange
        PasswordEncrypter sut = new PasswordEncrypterStub();

        // Act
        String encodedPassword = sut.encode("12345678!");
        boolean result = sut.matches("12345678!", encodedPassword);

        // Assert
        assertThat(result).isTrue();
    }

}
