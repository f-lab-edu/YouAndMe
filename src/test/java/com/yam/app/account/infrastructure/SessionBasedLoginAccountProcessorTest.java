package com.yam.app.account.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountNotFoundException;
import com.yam.app.account.domain.FakeAccountRepository;
import com.yam.app.account.domain.PasswordEncrypterStub;
import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.mock.web.MockHttpSession;

@DisplayName("세션 기반 회원 로그인 처리기")
final class SessionBasedLoginAccountProcessorTest {

    @TestFactory
    @DisplayName("회원 로그인 검증 테스트")
    Collection<DynamicTest> login_success() {
        //Arrange
        var fakeObject = new FakeAccountRepository();
        final var accountRepository = fakeObject;
        final var accountReader = fakeObject;
        var accountNotYetConfirm = Account.of("hello1@naver.com", "hello1",
            "password!");
        var accountCompleted = Account.of("hello@naver.com", "hello",
            "password!");
        accountCompleted.completeRegister();
        accountRepository.save(accountCompleted);
        accountRepository.save(accountNotYetConfirm);

        var sessionManager = new SessionManager(new MockHttpSession());
        var loginProcessor = new SessionBasedLoginAccountProcessor(accountReader,
            new PasswordEncrypterStub(), sessionManager);

        return Arrays.asList(
            dynamicTest("로그인이 성공적으로 완료되며, 세션에 값이 저장된다.", () -> {
                // Act
                loginProcessor.login("hello@naver.com", "password!");

                // Assert
                var result = sessionManager.fetchPrincipal();
                assertThat(result.isPresent()).isTrue();
            }),
            dynamicTest("이메일이 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginProcessor.login("dwqko@naver.com",
                        accountCompleted.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(AccountNotFoundException.class);
            }),
            dynamicTest("이메일은 유효하나 검증을 완료하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginProcessor.login(accountNotYetConfirm.getEmail(),
                        accountNotYetConfirm.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            }),
            dynamicTest("비밀번호가 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginProcessor.login(accountCompleted.getEmail(), "11111111!")
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
            })
        );
    }
}
