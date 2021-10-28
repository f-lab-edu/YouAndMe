package com.yam.app.integration;

import static com.yam.app.account.presentation.AccountApiUri.EMAIL_CONFIRM;
import static com.yam.app.account.presentation.AccountApiUri.FIND_INFO;
import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static com.yam.app.account.presentation.AccountApiUri.LOGOUT;
import static com.yam.app.account.presentation.AccountApiUri.REGISTER;
import static com.yam.app.account.presentation.AccountApiUri.UPDATE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.account.presentation.RegisterAccountCommand;
import com.yam.app.account.presentation.UpdateAccountCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("회원 모듈 통합 인수 테스트")
final class AccountIntegrationTests extends AbstractIntegrationTests {

    private static final String EMAIL_CONFIRM_SUCCESS_REDIRECT_URI = "http://localhost:3000/login";

    @Test
    @DisplayName("새로운 계정 등록에 적절한 파라미터가 입력되고, 계정이 성공적으로 등록된다.")
    void new_account_request_in_register_correctly() throws Exception {
        // Arrange
        var command = new RegisterAccountCommand();
        command.setEmail("msolo021015@gmail.com");
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
    @DisplayName("유효한 토큰과 이메일 정보를 통해 이메일 인증을 처리하고,"
        + "사용자의 기본 회원 정보를 기입하는 시나리오 테스트.")
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
    @DisplayName("이메일 검증 후 회워 정보 기입에서, 닉네임이 동일한 경우"
        + "랜덤한 값을 이어붙여서 저장하도록하는 시나리오 테스트")
    void duplicate_email_and_token_verify_request_in_correctly() throws Exception {
        // Act
        final var actions = mockMvc.perform(get(EMAIL_CONFIRM)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "emailchecktoken")
            .param("email", "jiwonDev@naver.com")
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

        // Act & Assert
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
                        .andExpect(jsonPath("$.data.nickname").isString())
                        .andExpect(jsonPath("$.data.image").isString());
                });
    }

    @Test
    @DisplayName("로그인에 적절한 파라미터를 입력하여, 성공하고 "
        + "로그아웃하는 시나리오 테스트.")
    void login_success_and_authentication_member_logout_scenarios() throws Exception {
        //Arrange
        var command = new LoginAccountCommand();
        command.setEmail("loginCheck@gmail.com");
        command.setPassword("password!");

        // Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(post(LOGOUT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                    );

                    actions
                        .andExpect(status().isOk());
                });
    }

    @Test
    @DisplayName("인증된 사용자가 자신의 정보를 수정하는 시나리오 테스트.")
    void should_authentication_user_info_update_success_scenarios() throws Exception {
        // Arrange
        var loginCommand = new LoginAccountCommand();
        loginCommand.setEmail("rebwon@gmail.com");
        loginCommand.setPassword("password!");
        var updateCommand = new UpdateAccountCommand();
        updateCommand.setNickname("jiwonKim");
        updateCommand.setPassword("password!2");
        updateCommand.setImage("jiwon.png");

        // Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(patch(UPDATE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand))
                    );

                    actions
                        .andExpect(status().isOk());
                }
            );
    }
}
