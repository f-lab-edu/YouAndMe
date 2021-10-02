package com.yam.app.article.presentation;

import static com.yam.app.article.presentation.ArticleApiUri.WRITE_ARTICLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.article.application.ArticleFacade;
import java.util.List;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("Article Command HTTP API")
@WebMvcTest(ArticleCommandApi.class)
final class ArticleCommandApiTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ArticleFacade articleFacade;

    private void assertThatInvalidArgumentError(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.message").value("Invalid argument"));
    }

    @Nested
    @DisplayName("게시글 작성 HTTP API")
    class WriteArticleApi {

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증되지 않은 사용자가 게시글을 작성하려고 시도하면 401 에러를 반환한다.")
        void not_authentication_user_write_article_fail(
            String args, List<String> list) throws Exception {
            var command = new WriteArticleCommand();
            command.setTitle(args);
            command.setContent(args);
            command.setImage(args);
            command.setTags(list);

            //Act
            final var actions = mockMvc.perform(post(WRITE_ARTICLE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Unauthorized request"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("인증된 사용자의 요청 Body가 null이거나 empty인 경우 400 에러를 반환한다.")
        void authentication_user_write_article_http_body_null_or_empty_fail(
            String args) throws Exception {
            // Arrange
            var session = new MockHttpSession();
            var command = new WriteArticleCommand();
            command.setTitle(args);
            command.setContent(args);
            command.setImage(args);

            // Act
            final var actions = mockMvc.perform(post(WRITE_ARTICLE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
                .session(session)
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증된 사용자의 요청 Body중 Tag의 개수가 3개 이상일 경우 400 에러를 반환한다.")
        void authentication_user_write_article_http_body_tag_size_over_fail(
            String args) throws Exception {
            // Arrange
            var session = new MockHttpSession();
            var command = new WriteArticleCommand();
            command.setTitle(args);
            command.setContent(args);
            command.setImage(args);
            command.setTags(List.of("good", "job", "sam", "kim"));

            // Act
            final var actions = mockMvc.perform(post(WRITE_ARTICLE)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
                .session(session)
            );

            // Assert
            assertThatInvalidArgumentError(actions);
        }
    }
}
