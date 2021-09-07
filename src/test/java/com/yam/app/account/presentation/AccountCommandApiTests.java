package com.yam.app.account.presentation;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
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

        private static final String EMAIL_AUTHORIZE_API = "/api/accounts/authorize";
        private static final String TOKEN = "token";
        private static final String EMAIL = "email";

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("HTTP 파라메타가 비었거나 null인 검증요청을 보낸 경우 400 HTTP Code 리턴한다.")
        void http_param_is_empty_or_null(String args) throws Exception {
            // Arrange
            var request = new ConfirmRegisterAccountRequest();
            request.setToken(args);
            request.setEmail(args);

            // Act
            final var actions = mockMvc.perform(get(EMAIL_AUTHORIZE_API)
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
            var request = new ConfirmRegisterAccountRequest();
            request.setToken(arg);
            request.setEmail(arg);

            // Act
            doThrow(IllegalStateException.class).when(accountFacade).registerConfirm(request);

            final var actions = mockMvc.perform(get(EMAIL_AUTHORIZE_API)
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

        @Test
        @DisplayName("토큰과 이메일 정보로 검증요청을 보낸 경우 303 HTTP Code 리턴한다.")
        void valid_success() throws Exception {
            // Arrange
            var request = new ConfirmRegisterAccountRequest();
            request.setToken("emailTOken");
            request.setEmail("jiwonDev@gmail.com");

            // Act
            final var actions = mockMvc.perform(get(EMAIL_AUTHORIZE_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .param(TOKEN, "emailTOken")
                .param(EMAIL, "jiwonDev@gmail.com")
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isSeeOther());
        }
    }

    @Nested
    @DisplayName("회원가입 등록 HTTP API")
    class RegisterApi {

        private static final String REGISTER_API = "/api/accounts";

        @Test
        @DisplayName("회원가입에 적절한 파라미터가 입력되고 회원가입이 성공한다.")
        void register_success() throws Exception {
            // Arrange
            var request = new RegisterAccountRequest();
            request.setEmail("msolo021015@gmail.com");
            request.setNickname("rebwon");
            request.setPassword("password!");

            // Act
            when(accountFacade.register(request)).thenReturn(
                new AccountResponse(1L, "msolo021015@gmail.com", "rebwon"));

            final var actions = mockMvc.perform(post(REGISTER_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").isString())
                .andExpect(jsonPath("$.nickname").isString());
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("Accept와 Content-Type을 지정하지 않아, HttpMediaTypeNotSupportedException 발생.")
        void register_account_api_not_use_accept_header_and_content_type(String arg)
            throws Exception {
            // Arrange
            var request = new RegisterAccountRequest();
            request.setEmail(arg);
            request.setNickname(arg);
            request.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(REGISTER_API)
                .content(objectMapper.writeValueAsString(request))
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
            var request = new RegisterAccountRequest();
            request.setEmail(arg);
            request.setNickname(arg);
            request.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(REGISTER_API)
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
        @AutoSource
        @DisplayName("HTTP 입력 파라미터가 이메일, 비밀번호 검증에 실패하여 에러를 응답한다.")
        void register_http_parameter_is_invalid_email_and_password(String arg) throws Exception {
            // Arrange
            var request = new RegisterAccountRequest();
            request.setEmail(arg);
            request.setNickname(arg);
            request.setPassword(arg);

            // Act
            final var actions = mockMvc.perform(post(REGISTER_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }
    }

}
