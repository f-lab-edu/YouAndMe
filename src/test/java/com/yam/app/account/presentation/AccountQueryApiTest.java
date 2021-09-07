package com.yam.app.account.presentation;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Account Query HTTP API")
@WebMvcTest(AccountQueryApi.class)
class AccountQueryApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountFacade accountFacade;

    @Nested
    @DisplayName("Login HTTP API")
    class LoginApi {

        @Test
        @DisplayName("정상적인 이메일과 비밀번호를 보내 로그인에 성공하고 200을 반환한다.")
        void login_success() throws Exception {
            //Arrange
            LoginAccountRequest request = new LoginAccountRequest();
            request.setEmail("jiwon@gmail.com");
            request.setPassword("password!");
            doNothing().when(accountFacade).login(request);

            //Act
            var actions = mockMvc.perform(post("/api/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isOk());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("로그인 요청 JSON 데이터가 null 이거나 비어있다면 400 에러를 반환한다.")
        void http_json_value_is_empty_or_null(String args) throws Exception {
            //Arrange
            var request = new LoginAccountRequest();
            request.setEmail(args);
            request.setPassword(args);

            //Act
            var actions = mockMvc.perform(post("/api/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            //Assert
            actions
                .andDo(print())
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("이메일과 비밀번호가 유효하지않아 로그인에 실패한 경우 401 에러를 반환한다.")
        void login_fail() throws Exception {
            // Arrange
            var request = new LoginAccountRequest();
            request.setEmail("wejiwef@naver.com");
            request.setPassword("DRFTGYHUJIKOL");
            doThrow(IllegalStateException.class).when(accountFacade).login(request);

            // Act
            final var actions = mockMvc.perform(post("/api/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // Assert
            actions
                .andDo(print())
                .andExpect(status().isUnauthorized());
        }
    }
}

