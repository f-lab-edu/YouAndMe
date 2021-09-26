package com.yam.app.account.presentation;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import org.javaunit.autoparams.AutoSource;
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
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("Account Command HTTP API")
@WebMvcTest(AccountCommandApi.class)
final class AccountCommandApiTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

    private void assertThatInvalidArgumentError(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.message").value("Invalid argument"));
    }

    @Nested
    @DisplayName("로그인 HTTP API")
    class LoginApi {

        @Test
        @DisplayName("세션이 없는 상태로 로그아웃을 요청하면 401 에러를 반환한다.")
        void logout() throws Exception {
            //Act
            final var actions = mockMvc.perform(post(AccountApiUri.LOGOUT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

            //Assert
            actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Unauthorized request"));
        }

        @Test
        @DisplayName("이메일, 비밀번호 형식은 유효하나 로그인이 실패한 경우 400 에러를 반환한다.")
        void login_fail() throws Exception {
            // Arrange
            var request = new LoginAccountCommand();
            request.setEmail("wejiwef@naver.com");
            request.setPassword("password1!");

            // Act
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            final var actions = mockMvc.perform(post(AccountApiUri.LOGIN)
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
        @ValueSource(strings = {"1", "a", "1a234567890123456"})
        @DisplayName("요청 Body의 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var request = new LoginAccountCommand();
            request.setEmail("jiwon22@gmail.com");
            request.setPassword(args);

            // Act
            final var actions = mockMvc.perform(post(AccountApiUri.LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
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
            final var actions = mockMvc.perform(post(AccountApiUri.LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
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
            final var actions = mockMvc.perform(post(AccountApiUri.LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            assertThatInvalidArgumentError(actions);
        }

    }

    @Nested
    @DisplayName("이메일 검증 HTTP API")
    class RegisterConfirmApi {

        private static final String TOKEN = "token";
        private static final String EMAIL = "email";

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("HTTP 파라메타가 비었거나 null인 검증요청을 보낸 경우 400 HTTP Code 리턴한다.")
        void http_param_is_empty_or_null(String args) throws Exception {
            // Arrange
            var command = new ConfirmRegisterAccountCommand();
            command.setToken(args);
            command.setEmail(args);

            // Act
            final var actions = mockMvc.perform(get(AccountApiUri.EMAIL_CONFIRM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param(TOKEN, args)
                .param(EMAIL, args)
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("HTTP 파라메타가 유효하지 않은 값으로 검증요청을 보낸 경우 400 HTTP Code 리턴한다.")
        void http_param_is_not_valid(String arg) throws Exception {
            // Arrange
            var command = new ConfirmRegisterAccountCommand();
            command.setToken(arg);
            command.setEmail(arg);

            // Act
            final var actions = mockMvc.perform(get(AccountApiUri.EMAIL_CONFIRM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param(TOKEN, arg)
                .param(EMAIL, arg)
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }
    }

    @Nested
    @DisplayName("회원가입 등록 HTTP API")
    class RegisterApi {

        @ParameterizedTest
        @AutoSource
        @DisplayName("Accept와 Content-Type을 지정하지 않아, HttpMediaTypeNotSupportedException 발생.")
        void register_account_api_not_use_accept_header_and_content_type(String arg)
            throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail(arg);
            command.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(AccountApiUri.REGISTER)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            actions
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Http media type not supported"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("HTTP 입력이 Null이거나 Emtpy인 경우 검증에 실패하여 에러를 응답한다.")
        void register_http_parameter_is_null_and_empty(String arg) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail(arg);
            command.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(AccountApiUri.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

        @ParameterizedTest
        @ValueSource(strings = {"@@@@@@@@@", "@naver.com", "jiwon"})
        @DisplayName("요청 Body의 이메일 형식이 맞지 않는 경우 400 에러를 반환한다.")
        void register_http_parameter_is_invalid_email_and_password(String arg) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail(arg);
            command.setPassword("password1!");

            // Act
            final var actions = mockMvc.perform(post(AccountApiUri.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "a", "1a234567890123456"})
        @DisplayName("요청 Body의 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail("jiwon@naver.com");
            command.setPassword(args);

            // Act
            final var actions = mockMvc.perform(post(AccountApiUri.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

    }

}
