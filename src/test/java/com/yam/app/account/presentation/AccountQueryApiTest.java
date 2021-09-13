package com.yam.app.account.presentation;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.infrastructure.AccountPrincipal;
import com.yam.app.account.infrastructure.LoginSessionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Account Query HTTP API")
@WebMvcTest(AccountQueryApi.class)
class AccountQueryApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

    @Nested
    @DisplayName("Login HTTP API")
    class LoginApi {

        @Test
        @DisplayName("로그인한 회원이 요청하면 세션에서 Account 정보를 성공적으로 반환 받는다.")
        void login_member_get_account_session_request() throws Exception {
            //Arrange
            var session = new MockHttpSession();
            session.setAttribute(LoginSessionUtils.LOGIN_ACCOUNT_EMAIL,
                new AccountPrincipal("loginCheck@gmail.com"));

            when(accountFacade.getLoginAccount("loginCheck@gmail.com"))
                .thenReturn(new AccountResponse(1L, "loginCheck@gmail.com",
                    "loginNick"));

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

        @Test
        @DisplayName("세션이 없는 상태로 Account 정보를 요청하면 401 에러를 반환한다.")
        void no_current_session_account_request() throws Exception {
            //Act
            final var actions = mockMvc.perform(get("/api/accounts/me")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("정상적인 이메일과 비밀번호를 보내 로그인에 성공하고 200을 반환한다.")
        void login_success() throws Exception {
            //Arrange
            LoginAccountRequestCommand request = new LoginAccountRequestCommand();
            request.setEmail("jiwon@gmail.com");
            request.setPassword("password!");
            doNothing().when(accountFacade).login(request);

            //Act
            final var actions = mockMvc.perform(post("/api/accounts/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("이메일, 비밀번호 형식은 유효하나 로그인이 실패한 경우 401 에러를 반환한다.")
        void login_fail() throws Exception {
            // Arrange
            var request = new LoginAccountRequestCommand();
            request.setEmail("wejiwef@naver.com");
            request.setPassword("password1!");
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            // Act
            final var actions = mockMvc.perform(post("/api/accounts/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "a", "1a234567890123456"})
        @DisplayName("요청 Body 의 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var request = new LoginAccountRequestCommand();
            request.setEmail("jiwon22@gmail.com");
            request.setPassword(args);
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            // Act
            final var actions = mockMvc.perform(post("/api/accounts/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"@@@@@@@@@", "@naver.com", "jiwon"})
        @DisplayName("요청 Body 의 이메일 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_email_is_invalid() throws Exception {
            // Arrange
            var request = new LoginAccountRequestCommand();
            request.setEmail("DQWJIDWQ291");
            request.setPassword("1abcabcabc");
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            // Act
            final var actions = mockMvc.perform(post("/api/accounts/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("요청 Body 의 이메일,비밀번호가 null 이거나 비어있다면 400 에러를 반환한다.")
        void http_json_value_is_empty_or_null(String args) throws Exception {
            //Arrange
            var request = new LoginAccountRequestCommand();
            request.setEmail(args);
            request.setPassword(args);

            //Act
            final var actions = mockMvc.perform(post("/api/accounts/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }
    }
}
