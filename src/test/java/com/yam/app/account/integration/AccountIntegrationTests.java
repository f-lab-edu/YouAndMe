package com.yam.app.account.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.presentation.RegisterAccountRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 통합 인수 테스트")
@ActiveProfiles("test")
final class AccountIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("새로운 계정을 등록하는 회원가입 시나리오")
    void register_success() throws Exception {
        // Arrange
        var request = new RegisterAccountRequest();
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
    void register_confirm() throws Exception {
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

}
