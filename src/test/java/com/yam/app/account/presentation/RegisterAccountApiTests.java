package com.yam.app.account.presentation;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("회원가입 등록 HTTP API")
@WebMvcTest(RegisterAccountApi.class)
class RegisterAccountApiTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

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
            new RegisterAccountResponse(1L, "msolo021015@gmail.com", "rebwon"));

        final var actions = mockMvc.perform(post("/api/register")
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
    @NullAndEmptySource
    @DisplayName("HTTP 입력이 Null이거나 Emtpy인 경우 검증에 실패하여 에러를 응답한다.")
    void register_http_parameter_is_null_and_empty(String arg) throws Exception {
        // Arrange
        var request = new RegisterAccountRequest();
        request.setEmail(arg);
        request.setNickname(arg);
        request.setPassword(arg);

        // Act
        final var actions = mockMvc.perform(post("/api/register")
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
        final var actions = mockMvc.perform(post("/api/register")
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
