package com.yam.app.integration;

import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static com.yam.app.article.presentation.ArticleApiUri.FIND_ALL;
import static com.yam.app.article.presentation.ArticleApiUri.FIND_BY_ID;
import static com.yam.app.article.presentation.ArticleApiUri.WRITE_ARTICLE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.article.presentation.WriteArticleCommand;
import java.util.List;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.MediaType;

@DisplayName("게시글 모듈 통합 인수 테스트")
final class ArticleIntegrationTests extends AbstractIntegrationTests {

    @ParameterizedTest
    @AutoSource
    @DisplayName("로그인에 적절한 파라미터를 입력하여, 성공하고 "
        + "인증된 사용자가 게시글 한 건을 작성하는 시나리오 테스트.")
    void login_success_and_authentication_member_logout_scenarios(String title, String content,
        String image, List<String> tags) throws Exception {
        //Arrange
        var loginCommand = new LoginAccountCommand();
        loginCommand.setEmail("loginCheck@gmail.com");
        loginCommand.setPassword("password!");

        var writeArticleCommand = new WriteArticleCommand();
        writeArticleCommand.setTitle(title);
        writeArticleCommand.setContent(content);
        writeArticleCommand.setImage(image);
        writeArticleCommand.setTags(tags);

        // Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(post(WRITE_ARTICLE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writeArticleCommand))
                    );

                    actions
                        .andExpect(status().isCreated());
                });
    }

    @Test
    @DisplayName("기본적으로 아무 페이징 조건을 주지 않을 경우, "
        + "20건을 생성 날짜 내림차순으로 정렬하여 보여준다.")
    void default_main_page_find_all_preview_article_response() throws Exception {
        // Act
        mockMvc.perform(get(FIND_ALL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    @ParameterizedTest
    @AutoSource
    @DisplayName("로그인에 적절한 파라미터를 입력하여, 성공하고 "
        + "인증된 사용자가 존재하는 게시글, 존재하지 않는 게시글을 각각 조회하는 시나리오 테스트.")
    void login_success_and_get_article_by_id() throws Exception {
        //Arrange
        var loginCommand = new LoginAccountCommand();
        loginCommand.setEmail("loginCheck@gmail.com");
        loginCommand.setPassword("password!");

        var articleId = 1L;
        var invalidArticleId = 9999L;

        // Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(get(FIND_BY_ID + articleId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                    );

                    actions
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").isNumber())
                        .andExpect(jsonPath("$.authorId").isNumber())
                        .andExpect(jsonPath("$.title").isString())
                        .andExpect(jsonPath("$.content").isString())
                        .andExpect(jsonPath("$.image").isString())
                        .andExpect(jsonPath("$.tags").isArray());
                })
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(get(FIND_BY_ID + invalidArticleId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                    );

                    actions
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.success").value(false))
                        .andExpect(jsonPath("$.data").doesNotExist());
                });
    }
}
