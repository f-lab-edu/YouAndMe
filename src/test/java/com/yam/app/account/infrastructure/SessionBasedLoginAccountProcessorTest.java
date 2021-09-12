package com.yam.app.account.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.account.common.SessionConst;
import com.yam.app.account.domain.Account;
import com.yam.app.account.domain.AccountPrincipal;
import com.yam.app.account.domain.FakeAccountRepository;
import com.yam.app.account.domain.PasswordEncrypter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
final class SessionBasedLoginAccountProcessorTest {

    @Autowired
    private MockMvc mockMvc;

    @TestFactory
    @DisplayName("세션 회원 로그인 검증 테스트")
    Collection<DynamicTest> login_success() throws Exception {
        //Arrange
        var accountRepository = new FakeAccountRepository();
        var accountNotConfirm = Account.of("hello1@naver.com", "hello1",
            "password!");
        var accountCompleted = Account.of("hello@naver.com", "hello",
            "password!");
        accountCompleted.completeRegister();
        accountRepository.save(accountCompleted);
        accountRepository.save(accountNotConfirm);

        var session = new FakeHttpSession();
        var passwordEncryptor = new FakePasswordEncrypter();
        var loginAccountProcessor = new SessionBasedLoginAccountProcessor(accountRepository,
            passwordEncryptor, session);

        return Arrays.asList(
            dynamicTest("회원 로그인에 성공한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login(accountCompleted.getEmail(),
                        accountCompleted.getPassword())
                );

                // Assert
                assertThat(throwable).isNull();
                assertThat(session.getAttribute(SessionConst.LOGIN_ACCOUNT_PRINCIPAL)).isInstanceOf(
                    AccountPrincipal.class);
            }),
            dynamicTest("이메일이 유효하지 않은 경우 예외를 리턴한다.", () -> {
                // Act
                var throwable = catchThrowable(
                    () -> loginAccountProcessor.login("dwqko@naver.com",
                        accountCompleted.getPassword())
                );

                // Assert
                assertThat(throwable).isInstanceOf(IllegalStateException.class);
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

    @Test
    @DisplayName("로그인한 회원이 세션에서 자신의 정보를 받아오는 시나리오")
    void login_member_get_account_session_request() throws Exception {
        //Arrange
        var session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_ACCOUNT_PRINCIPAL,
            new LoginAccountPrincipal("loginCheck@gmail.com"));

        //Act
        final var actions = mockMvc.perform(get("/api/accounts/me")
            .session(session)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON));

        //Assert
        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").isString())
            .andExpect(jsonPath("$.nickname").isString());

        session.clearAttributes();
    }

    private static class FakePasswordEncrypter implements PasswordEncrypter {

        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString();
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword);
        }
    }

    private static class FakeHttpSession implements HttpSession {

        private final Map<String, Object> sessionData = new ConcurrentHashMap<>();

        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public String getId() {
            return null;
        }

        @Override
        public long getLastAccessedTime() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public int getMaxInactiveInterval() {
            return 0;
        }

        @Override
        public void setMaxInactiveInterval(int interval) {

        }

        @Override
        public HttpSessionContext getSessionContext() {
            return null;
        }

        @Override
        public Object getAttribute(String name) {
            return sessionData.getOrDefault(name, null);
        }

        @Override
        public Object getValue(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String[] getValueNames() {
            return new String[0];
        }

        @Override
        public void setAttribute(String name, Object value) {
            sessionData.put(name, value);
        }

        @Override
        public void putValue(String name, Object value) {

        }

        @Override
        public void removeAttribute(String name) {

        }

        @Override
        public void removeValue(String name) {

        }

        @Override
        public void invalidate() {

        }

        @Override
        public boolean isNew() {
            return false;
        }
    }
}
