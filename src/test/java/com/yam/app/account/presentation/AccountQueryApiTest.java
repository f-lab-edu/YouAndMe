package com.yam.app.account.presentation;

import static com.yam.app.account.presentation.AccountApiUri.FIND_INFO;
import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
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
        @DisplayName("인증되지 않은 상태로 요청을 보낸 경우 401 에러를 반환한다.")
        void no_current_session_account_request() throws Exception {
            //Act
            final var actions = mockMvc.perform(get(FIND_INFO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("이메일, 비밀번호 형식은 유효하나 로그인이 실패한 경우 401 에러를 반환한다.")
        void login_fail() throws Exception {
            // Arrange
            var request = new LoginAccountCommand();
            request.setEmail("wejiwef@naver.com");
            request.setPassword("password1!");

            // Act
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            final var actions = mockMvc.perform(post(LOGIN)
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
        @DisplayName("요청 Body의 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var request = new LoginAccountCommand();
            request.setEmail("jiwon22@gmail.com");
            request.setPassword(args);

            // Act
            final var actions = mockMvc.perform(post(LOGIN)
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
        @DisplayName("요청 Body의 이메일 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_email_is_invalid(String email) throws Exception {
            // Arrange
            var command = new LoginAccountCommand();
            command.setEmail(email);
            command.setPassword("password!1");

            // Act
            final var actions = mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("요청 Body의 이메일,비밀번호가 null 이거나 비어있다면 400 에러를 반환한다.")
        void http_json_value_is_empty_or_null(String args) throws Exception {
            //Arrange
            var command = new LoginAccountCommand();
            command.setEmail(args);
            command.setPassword(args);

            //Act
            final var actions = mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }
    }
}
