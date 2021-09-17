package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Arrays;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("회원 검증 도메인 서비스")
final class ConfirmRegisterAccountProcessorTest {

    @TestFactory
    @DisplayName("이메일 검증 시나리오")
    Collection<DynamicTest> verify_account_scenarios() {
        final var fakeObject = new FakeAccountRepository();
        final var accountRepository = fakeObject;
        final var accountReader = fakeObject;
        final var tokenVerifier = new TokenVerifier(accountReader);
        final var confirmRegisterAccountProcessor = new ConfirmRegisterAccountProcessor(
            accountReader, accountRepository, tokenVerifier);

        accountRepository.save(Account.of("jiwonDev@gmail.com", "jiwon", "password!"));
        final var account = accountReader.findByEmail("jiwonDev@gmail.com").get();

        return Arrays.asList(
            DynamicTest.dynamicTest("회원 이메일 토큰검증에 성공한다.", () -> {
                // Act
                confirmRegisterAccountProcessor.registerConfirm(account.getEmailCheckToken(),
                    account.getEmail());
                var updatedAccount = accountRepository.findByEmail(account.getEmail()).get();

                // Assert
                assertThat(updatedAccount.isEmailVerified()).isTrue();
            }),
            DynamicTest.dynamicTest("이메일이나 토큰의 값이 null인 경우 예외를 리턴한다.",
                () -> {

                    // Act & Assert
                    assertThatExceptionOfType(NullPointerException.class)
                        .isThrownBy(
                            () -> confirmRegisterAccountProcessor.registerConfirm(null, null));
                }),
            DynamicTest.dynamicTest("이메일 검증에 실패하여 예외를 리턴한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> confirmRegisterAccountProcessor.registerConfirm(
                            account.getEmailCheckToken(),
                            "HiIamNotExistEmail@naver.com"));
                }),
            DynamicTest.dynamicTest("토큰 검증에 실패하여 예외를 리턴한다.",
                () -> {
                    // Act & Assert
                    assertThatExceptionOfType(IllegalStateException.class)
                        .isThrownBy(
                            () -> confirmRegisterAccountProcessor.registerConfirm("안녕난가짜토큰이라고해",
                                account.getEmail()));
                })
        );
    }
}
