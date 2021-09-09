package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.yam.app.account.domain.PasswordEncrypterTest.PasswordEncrypterStub;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("회원 로그인 도메인 서비스")
class LoginAccountProcessorTest {

    @TestFactory
    @DisplayName("회원 로그인 시나리오")
    Collection<DynamicTest> login_success() throws Exception {
        //Arrange
        var accountRepository = new FakeAccountRepository();
        var accountReader = accountRepository;
        var passwordEncrypter = new PasswordEncrypterStub();
        var loginAccountProcessor = new LoginAccountProcessor(accountReader, passwordEncrypter);

        var account = Account.of("hello@naver.com", "hello", "password!");
        account.completeRegister();
        accountRepository.save(account);

        return Arrays.asList(
            dynamicTest("회원 로그인에 성공한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login(account.getEmail(), account.getPassword())
                );

                // Assert
                assertThat(throwable).isNull();
            }),
            dynamicTest("이메일이 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login("dwqko@naver.com", account.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            }),
            dynamicTest("비밀번호가 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login(account.getEmail(), "11111111!")
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            })
        );
    }

}
