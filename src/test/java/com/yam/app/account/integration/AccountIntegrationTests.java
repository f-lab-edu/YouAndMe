package com.yam.app.account.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.presentation.RegisterAccountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 통합 인수 테스트")
@ActiveProfiles("test")
final class AccountIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = MockMvcWebTestClient
            .bindTo(mockMvc)
            .build();
    }

    @Test
    @DisplayName("새로운 계정을 등록하는 회원가입 시나리오")
    void register_success() throws Exception {
        // Arrange
        var request = new RegisterAccountRequest();
        request.setEmail("msolo021015@gmail.com");
        request.setNickname("rebwon");
        request.setPassword("password!");

        // Act
        var spec = webTestClient
            .post()
            .uri("/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(request))
            .exchange();

        // Assert
        spec
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isNumber()
            .jsonPath("$.email").isNotEmpty()
            .jsonPath("$.nickname").isNotEmpty();
    }
}
