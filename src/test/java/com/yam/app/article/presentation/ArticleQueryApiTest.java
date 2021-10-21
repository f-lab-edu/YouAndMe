package com.yam.app.article.presentation;

import static com.yam.app.article.presentation.ArticleApiUri.FIND_BY_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.article.application.ArticleFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Article Qurey HTTP API")
@WebMvcTest(ArticleQueryApi.class)
@ActiveProfiles("test")
class ArticleQueryApiTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleFacade articleFacade;

    @Test
    @DisplayName("인증되지 않은 사용자가 게시글 조회 요청을 보냈다면 401에러를 반환한다.")
    void unauthenticated_user_request() throws Exception {
        //Act
        final var actions = mockMvc.perform(get(FIND_BY_ID + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
        );

        //Assert
        actions
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.message").value("Unauthorized request"));

    }
}
