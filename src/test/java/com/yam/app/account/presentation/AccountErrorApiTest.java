package com.yam.app.account.presentation;

import static com.yam.app.account.infrastructure.AccountApiUri.UNAUTHORIZED_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.common.GlobalApiExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("Account Error HTTP API")
@AutoConfigureWebMvc
final class AccountErrorApiTest {

    private final AccountErrorApi accountErrorApi = new AccountErrorApi();
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountErrorApi)
            .setControllerAdvice(GlobalApiExceptionHandler.class)
            .alwaysDo(print())
            .build();
    }

    @Test
    @DisplayName("인증되지 않은 상태로 UNAUTHORIZED_REQUEST 를 요청한 경우 401 에러를 반환한다.")
    void no_current_session_account_request() throws Exception {
        //Act
        final var actions = mockMvc.perform(get(UNAUTHORIZED_REQUEST)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON));

        //Assert
        actions
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.message").value("Unauthorized request"));
    }

}
