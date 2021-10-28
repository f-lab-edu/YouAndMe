package com.yam.app.account.presentation;

import static com.yam.app.account.presentation.AccountApiUri.EMAIL_CONFIRM;
import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static com.yam.app.account.presentation.AccountApiUri.LOGOUT;
import static com.yam.app.account.presentation.AccountApiUri.REGISTER;
import static com.yam.app.account.presentation.AccountApiUri.UPDATE;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.extension.WebApiTestExtension;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

@DisplayName("Account Command HTTP API")
final class AccountCommandApiTests extends WebApiTestExtension {

    @Nested
    @DisplayName("회원 정보 수정 HTTP API")
    class UpdateAccountApi {

        @Test
        @DisplayName("인증되지 않은 사용자가 정보 변경을 요청하면 401 에러를 반환한다.")
        void not_authentication_update_command() throws Exception {
            var command = new UpdateAccountCommand();
            command.setImage("temp.png");
            command.setNickname("jiwon");
            command.setPassword("password!2");

            //Act
            final var actions = mockMvc.perform(patch(UPDATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Unauthorized request"));
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "a", "1a234567890123456"})
        @DisplayName("인증된 사용자의 요청 Body 비밀번호 형식이 맞지 않은 경우 400 에러를 반환한다.")
        void http_json_password_is_invalid(String args) throws Exception {
            // Arrange
            var session = new MockHttpSession();
            var command = new UpdateAccountCommand();
            command.setImage("temp.png");
            command.setNickname("jiwon");
            command.setPassword(args);

            // Act
            final var actions = mockMvc.perform(patch(UPDATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
                .session(session)
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("인증된 사용자의 요청 Body 정보가 null 혹은 empty인 경우 400 에러를 반환한다.")
        void http_json_value_is_empty_or_null(String args) throws Exception {
            //Arrange
            var session = new MockHttpSession();
            var command = new UpdateAccountCommand();
            command.setNickname(args);
            command.setPassword(args);
            command.setImage(args);

            //Act
            final var actions = mockMvc.perform(patch(UPDATE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
                .session(session)
            );

            //Assert
            assertThatInvalidArgumentError(actions);
        }

    }

    @Nested
    @DisplayName("로그인 HTTP API")
    class LoginApi {

        @Test
        @DisplayName("세션이 없는 상태로 로그아웃을 요청하면 401 에러를 반환한다.")
        void logout() throws Exception {
            //Act
            final var actions = mockMvc.perform(post(LOGOUT)
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
            final var actions = mockMvc.perform(post(LOGIN)
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
            final var actions = mockMvc.perform(post(LOGIN)
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
            final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
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
            final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
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
            final var actions = mockMvc.perform(post(REGISTER)
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
            final var actions = mockMvc.perform(post(REGISTER)
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
            final var actions = mockMvc.perform(post(REGISTER)
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
            final var actions = mockMvc.perform(post(REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

    }

}
