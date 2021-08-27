package com.yam.app.account.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.yam.app.account.application.RegisterAccountCommand;
import com.yam.app.account.domain.PasswordEncrypterTest.PasswordEncrypterStub;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

@DisplayName("회원가입 도메인 서비스")
class RegisterAccountProcessorTest {

    @TestFactory
    @DisplayName("회원가입 시나리오")
    Collection<DynamicTest> register_account_scenarios() {
        var repository = new AccountRepositoryStub();
        var processor = new RegisterAccountProcessor(repository, new PasswordEncrypterStub());
        return Arrays.asList(
            DynamicTest.dynamicTest("회원가입에 성공한다.", () -> {
                // Arrange
                var command = new RegisterAccountCommand("rebwon@gmail.com", "rebwon", "password!");

                // Act
                Account account = processor.process(command.getEmail(), command.getNickname(),
                    command.getPassword());

                // Assert
                assertThat(account.getId()).isEqualTo(1L);
            }),
            DynamicTest.dynamicTest("이메일 검증에 실패하여 예외를 리턴한다.", () -> {
                // Arrange
                var command = new RegisterAccountCommand("rebwon@gmail.com", "rebwon", "password!");

                // Act & Assert
                assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> processor.process(command.getEmail(), command.getNickname(),
                        command.getPassword()));
            }),
            DynamicTest.dynamicTest("닉네임 검증에 실패하여 예외를 리턴한다.", () -> {
                // Arrange
                var command = new RegisterAccountCommand("kitty@gmail.com", "rebwon", "password!");

                // Act & Assert
                assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> processor.process(command.getEmail(), command.getNickname(),
                        command.getPassword()));
            })
        );
    }

    private static class AccountRepositoryStub implements AccountRepository {

        private final Map<Long, Account> data = new ConcurrentHashMap<>();
        private final AtomicLong idGenerator = new AtomicLong();

        @Override
        public boolean existsByEmail(String email) {
            return data.values().stream()
                .anyMatch(account -> account.getEmail().equals(email));
        }

        @Override
        public boolean existsByNickname(String nickname) {
            return data.values().stream()
                .anyMatch(account -> account.getNickname().equals(nickname));
        }

        @Override
        public Account save(Account entity) {
            if (entity.getId() == null) {
                entity.setId(idGenerator.incrementAndGet());
                data.put(entity.getId(), entity);
            }
            return data.putIfAbsent(entity.getId(), entity);
        }
    }
}
