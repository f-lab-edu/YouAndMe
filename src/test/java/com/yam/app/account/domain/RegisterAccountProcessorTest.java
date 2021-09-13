package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("회원가입 도메인 서비스")
final class RegisterAccountProcessorTest {

    @TestFactory
    @DisplayName("회원가입 시나리오")
    Collection<DynamicTest> register_account_scenarios() {
        var fakeObject = new FakeAccountRepository();
        var repository = fakeObject;
        var reader = fakeObject;
        var processor = new RegisterAccountProcessor(repository,
            reader, new PasswordEncrypterStub());
        return Arrays.asList(
            DynamicTest.dynamicTest("회원가입에 성공한다.", () -> {
                // Act
                var account = processor.process("rebwon@gmail.com", "rebwon", "password!");

                // Assert
                assertThat(account.getId()).isEqualTo(1L);
            }),
            DynamicTest.dynamicTest("이메일 검증에 실패하여 예외를 리턴한다.", () -> {
                // Act & Assert
                assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> processor.process("rebwon@gmail.com", "rebwon", "password!"));
            }),
            DynamicTest.dynamicTest("닉네임 검증에 실패하여 예외를 리턴한다.", () -> {
                // Act & Assert
                assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> processor.process("kitty@gmail.com", "rebwon", "password!"));
            })
        );
    }
}
