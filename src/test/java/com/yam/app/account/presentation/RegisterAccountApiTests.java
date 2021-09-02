package com.yam.app.account.presentation;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

@DisplayName("회원가입 등록 HTTP API")
@WebMvcTest(RegisterAccountApi.class)
class RegisterAccountApiTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = MockMvcWebTestClient
            .bindTo(mockMvc)
            .build();
    }

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

    @Test
    @DisplayName("Accept 헤더와 Content-Type을 지정해주지 않았으므로, HttpMediaTypeNotSupportedException이 발생한다.")
    void register_account_api_not_use_accept_header_and_content_type() throws Exception {
        // Arrange
        var request = new RegisterAccountRequest();
        request.setEmail("msolo021015@gmail.com");
        request.setNickname("rebwon");
        request.setPassword("password!");

        // Act
        var spec = webTestClient
            .post()
            .uri("/api/accounts")
            .bodyValue(objectMapper.writeValueAsString(request))
            .exchange();

        // Assert
        spec
            .expectStatus().isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
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
        var spec = webTestClient
            .post()
            .uri("/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(request))
            .exchange();

        // Assert
        spec
            .expectStatus().isBadRequest();
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
        var spec = webTestClient
            .post()
            .uri("/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(objectMapper.writeValueAsString(request))
            .exchange();

        // Assert
        spec
            .expectStatus().isBadRequest();
    }
}
