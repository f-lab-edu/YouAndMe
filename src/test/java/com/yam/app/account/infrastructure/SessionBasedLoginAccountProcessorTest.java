package com.yam.app.account.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.FakeAccountRepository;
import com.yam.app.account.domain.PasswordEncrypterStub;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

final class SessionBasedLoginAccountProcessorTest {

    @TestFactory
    @DisplayName("세션 회원 로그인 검증 테스트")
    Collection<DynamicTest> login_success() {
        //Arrange
        var accountRepository = new FakeAccountRepository();
        var accountNotConfirm = Account.of("hello1@naver.com", "hello1",
            "password!");
        var accountCompleted = Account.of("hello@naver.com", "hello",
            "password!");
        accountCompleted.completeRegister();
        accountRepository.save(accountCompleted);
        accountRepository.save(accountNotConfirm);

        var passwordEncryptor = new PasswordEncrypterStub();
        var loginAccountProcessor = new SessionBasedLoginAccountProcessor(accountRepository,
            passwordEncryptor);

        return Arrays.asList(
            dynamicTest("이메일이 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login("dwqko@naver.com",
                        accountCompleted.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
            }),
            dynamicTest("이메일은 유효하나 검증을 완료하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login(accountNotConfirm.getEmail(),
                        accountNotConfirm.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            }),
            dynamicTest("비밀번호가 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login(accountCompleted.getEmail(), "11111111!")
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            })
        );
    }
}
