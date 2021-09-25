package com.yam.app.account.presentation;

import static com.yam.app.account.infrastructure.AccountApiUri.FIND_INFO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.account.application.AccountFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Account Query HTTP API")
@WebMvcTest(AccountQueryApi.class)
class AccountQueryApiTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountFacade accountFacade;

    @ParameterizedTest
    @ValueSource(strings = {FIND_INFO})
    @DisplayName("로그인을 하지 않은 경우 인증이 필요한 다른 URI 에 접근할 수 없다.")
    void no_current_session_account_request(String uri) throws Exception {
        //Act
        final var actions = mockMvc.perform(get(uri)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON));

        //Assert
        actions
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist());
    }

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
        @DisplayName("잘못된 인증 정보로 요청을 보낸 경우 401 응답을 반환한다.")
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
