package com.yam.app.account.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountReader;
import com.yam.app.account.domain.AccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class MybatisAccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountReader accountReader;

    @Test
    @DisplayName("이메일 중복 쿼리 학습 테스트")
    void existsByEmail() {
        boolean result = accountRepository.existsByEmail("jiwonDev@gmail.com");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("닉네임 중복 쿼리 학습 테스트")
    void existsByNickname() {
        boolean result = accountRepository.existsByNickname("jiwon");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("사용자 이메일을 사용한 조회 테스트")
    void findByEmail() {
        Account account = accountReader.findByEmail("jiwonDev@gmail.com");

        assertThat(account.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("Account 객체 저장 테스트")
    void save() {
        Account account = accountRepository.save(
            Account.of("rebwon@gmail.com", "rebwon", "password!"));

        assertThat(account.getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("Account 객체 갱신 테스트")
    void update() {
        Account account = accountRepository.save(
            Account.of("jiwon22@gmail.com", "jiwon2", "password!"));

        account.completeRegister();
        Account updatedAccount = accountRepository.update(account);

        assertThat(updatedAccount.isEmailVerified()).isTrue();
    }
}
