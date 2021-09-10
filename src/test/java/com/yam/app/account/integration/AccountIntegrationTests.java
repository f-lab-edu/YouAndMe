package com.yam.app.account.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.presentation.LoginAccountRequestCommand;
import com.yam.app.account.presentation.LoginSessionUtils;
import com.yam.app.account.presentation.RegisterAccountRequestCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    private MockHttpSession session;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
        session.setAttribute(LoginSessionUtils.LOGIN_ACCOUNT_EMAIL, "loginCheck@gmail.com");
    }

    @AfterEach
    public void clean() {
        session.clearAttributes();
    }

    @Test
    @DisplayName("새로운 계정을 등록하는 회원가입 시나리오")
    void new_account_request_in_register_correctly() throws Exception {
        // Arrange
        var request = new RegisterAccountRequestCommand();
        request.setEmail("msolo021015@gmail.com");
        request.setNickname("rebwon");
        request.setPassword("password!");

        // Act
        final var actions = mockMvc.perform(post("/api/accounts")
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

    @Test
    @DisplayName("이메일과 토큰을 검증하고 회원의 상태를 업데이트하는 시나리오")
    void email_and_token_verify_request_in_correctly() throws Exception {
        // Act
        final var actions = mockMvc.perform(get("/api/accounts/authorize")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .param("token", "emailchecktoken")
            .param("email", "jiwonDev@gmail.com")
        );

        // Assert
        actions
            .andDo(print())
            .andExpect(status().isSeeOther())
            .andExpect(redirectedUrl("http://localhost:3000/login"));
    }

    @Test
    @DisplayName("로그인 요청을 성공하여 서버의 세션 등록을 완료하는 시나리오")
    void login_success_get_session() throws Exception {
        //Arrange
        LoginAccountRequestCommand request = new LoginAccountRequestCommand();
        request.setEmail("loginCheck@gmail.com");
        request.setPassword("password!");

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
    @DisplayName("로그인한 회원이 세션에서 자신의 정보를 받아오는 시나리오")
    void login_member_get_account_session_request() throws Exception {
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

    }

}
