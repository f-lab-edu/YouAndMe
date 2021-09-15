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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.infrastructure.AccountPrincipal;
import com.yam.app.account.infrastructure.SessionManager;
import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.account.presentation.RegisterAccountCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").isString())
            .andExpect(jsonPath("$.nickname").isString());
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
    @DisplayName("로그인에 적절한 파라미터를 입력하고 로그인 요청이 성공적으로 완료된다.")
    void login_success() throws Exception {
        //Arrange
        var command = new LoginAccountCommand();
        command.setEmail("loginCheck@gmail.com");
        command.setPassword("password!");

        //Act
        final var actions = mockMvc.perform(post(LOGIN)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(command))
        );

        //Assert
        actions
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증된 기본 사용자가 자신의 정보를 조회한다.")
    void authentication_member_find_info_success() throws Exception {
        //Arrange
        var session = new MockHttpSession();
        var sessionManager = new SessionManager(session);
        sessionManager.setPrincipal(new AccountPrincipal("loginCheck@gmail.com"));

        //Act
        final var actions = mockMvc.perform(get(FIND_INFO)
            .session(session)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        );

        //Assert
        actions
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.email").isString())
            .andExpect(jsonPath("$.nickname").isString());
    }

}
