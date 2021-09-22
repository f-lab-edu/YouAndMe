package com.yam.app.account.integration;

import static com.yam.app.account.presentation.AccountApiUri.EMAIL_CONFIRM;
import static com.yam.app.account.presentation.AccountApiUri.FIND_INFO;
import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static com.yam.app.account.presentation.AccountApiUri.REGISTER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.account.presentation.RegisterAccountCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 통합 인수 테스트")
@ActiveProfiles("test")
final class AccountIntegrationTests {

    private static final String EMAIL_CONFIRM_SUCCESS_REDIRECT_URI = "http://localhost:3000/login";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(wac)
            .apply(sharedHttpSession())
            .build();
    }

    @Test
    @DisplayName("새로운 계정 등록에 적절한 파라미터가 입력되고, 계정이 성공적으로 등록된다.")
    void new_account_request_in_register_correctly() throws Exception {
        // Arrange
        var command = new RegisterAccountCommand();
        command.setEmail("msolo021015@gmail.com");
        command.setNickname("rebwon");
        command.setPassword("password!");

        // Act
        final var actions = mockMvc.perform(post(REGISTER)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(command))
        );

        // Assert
        actions
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("이메일 인증에 적절한 토큰과 이메일 정보가 입력되고, 이메일 인증 상태가 성공적으로 압데이트 된다.")
    void email_and_token_verify_request_in_correctly() throws Exception {
        // Act
        final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "emailchecktoken")
            .param("email", "jiwonDev@gmail.com")
        );

        // Assert
        actions
            .andDo(print())
            .andExpect(status().isSeeOther())
            .andExpect(redirectedUrl(EMAIL_CONFIRM_SUCCESS_REDIRECT_URI));
    }

    @Test
    @DisplayName("로그인에 적절한 파라미터를 입력하여, 성공하고 "
        + "이후 자신의 정보를 조회하는 시나리오 테스트.")
    void login_success_and_authentication_member_find_info_success_scenarios() throws Exception {
        //Arrange
        var command = new LoginAccountCommand();
        command.setEmail("loginCheck@gmail.com");
        command.setPassword("password!");

        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(get(FIND_INFO)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                    );

                    actions
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.success").value(true))
                        .andExpect(jsonPath("$.message").doesNotExist())
                        .andExpect(jsonPath("$.data.id").isNumber())
                        .andExpect(jsonPath("$.data.email").isString())
                        .andExpect(jsonPath("$.data.nickname").isString());
                });
    }

}
