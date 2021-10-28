package com.yam.app.account.presentation;

import static com.yam.app.account.presentation.AccountApiUri.FIND_INFO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.extension.WebApiTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

@DisplayName("Account Query HTTP API")
class AccountQueryApiTest extends WebApiTestExtension {

    @Nested
    @DisplayName("사용자 조회 HTTP API")
    class LoginApi {

        @Test
        @DisplayName("인증되지 않은 상태로 요청을 보낸 경우 401 에러를 반환한다.")
        void no_current_session_account_request() throws Exception {
            //Act
            final var actions = mockMvc.perform(get(FIND_INFO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

            //Assert
            actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Unauthorized request"));
        }

        @Test
        @DisplayName("잘못된 인증 정보로 요청을 보낸 경우 401 에러를 반환한다.")
        void failed_fetch_session_principal() throws Exception {
            //Act
            var session = new MockHttpSession();

            final var actions = mockMvc.perform(get(FIND_INFO)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .session(session)
            );

            //Assert
            actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Failed fetch principal"));
        }
    }
}
