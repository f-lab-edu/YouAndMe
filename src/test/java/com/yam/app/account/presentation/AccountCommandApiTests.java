package com.yam.app.account.presentation;

import static com.yam.app.account.presentation.AccountApiUri.EMAIL_CONFIRM;
import static com.yam.app.account.presentation.AccountApiUri.REGISTER;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Account Command HTTP API")
@WebMvcTest(AccountCommandApi.class)
final class AccountCommandApiTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

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
            final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param(TOKEN, args)
                .param(EMAIL, args)
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
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
            doThrow(IllegalStateException.class).when(accountFacade).registerConfirm(command);

            final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param(TOKEN, arg)
                .param(EMAIL, arg)
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
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
            command.setNickname(arg);
            command.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(REGISTER)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            actions
                .andExpect(status().isUnsupportedMediaType());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("HTTP 입력이 Null이거나 Emtpy인 경우 검증에 실패하여 에러를 응답한다.")
        void register_http_parameter_is_null_and_empty(String arg) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail(arg);
            command.setNickname(arg);
            command.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(REGISTER)
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
        @ValueSource(strings = {"@@@@@@@@@", "@naver.com", "jiwon"})
        @DisplayName("요청 Body의 이메일 형식이 맞지 않는 경우 400 에러를 반환한다.")
        void register_http_parameter_is_invalid_email_and_password(String arg) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail(arg);
            command.setNickname("jiwon");
            command.setPassword("password1!");

            // Act
            final var actions = mockMvc.perform(post(REGISTER)
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
        @ValueSource(strings = {"1", "a", "1a234567890123456"})
        @DisplayName("요청 Body의 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var command = new RegisterAccountCommand();
            command.setEmail("jiwon@naver.com");
            command.setNickname("jiwon");
            command.setPassword(args);

            // Act
            final var actions = mockMvc.perform(post(REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

    }

}
