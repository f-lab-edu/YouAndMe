package com.yam.app.integration;

import static com.yam.app.account.presentation.AccountApiUri.LOGIN;
import static com.yam.app.comment.presentation.CommentApiUri.CREATE_COMMENT;
import static com.yam.app.comment.presentation.CommentApiUri.UPDATE_COMMENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yam.app.account.presentation.LoginAccountCommand;
import com.yam.app.comment.presentation.CreateCommentCommand;
import com.yam.app.comment.presentation.UpdateCommentCommand;
import org.javaunit.autoparams.AutoSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.http.MediaType;

@DisplayName("댓글 모듈 통합 인수 테스트")
final class CommentIntegrationTests extends AbstractIntegrationTests {

    @ParameterizedTest
    @AutoSource
    @DisplayName("로그인에 적절한 파라미터를 입력하여 성공하고 인증된 사용자가 댓글을 작성하는 시나리오 테스트")
    void login_success_and_create_comment_scenarios(String args) throws Exception {
        //Arrange
        var loginCommand = new LoginAccountCommand();
        loginCommand.setEmail("loginCheck@gmail.com");
        loginCommand.setPassword("password!");

        var createCommand = new CreateCommentCommand();
        createCommand.setArticleId(1L);
        createCommand.setContent(args);

        //Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(post(CREATE_COMMENT)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommand))
                    );

                    actions
                        .andDo(print())
                        .andExpect(status().isCreated());
                });

    }

    @ParameterizedTest
    @AutoSource
    @DisplayName("로그인에 적절한 파라미터를 입력하여 성공하고 인증된 사용자가 댓글을 수정하는 시나리오 테스트")
    void login_success_and_update_comment_and_then_delete_it_scenarios(String args)
        throws Exception {
        //Arrange
        var loginCommand = new LoginAccountCommand();
        loginCommand.setEmail("comment@gmail.com");
        loginCommand.setPassword("password!");

        var updateCommentId = 1L;
        var updateCommand = new UpdateCommentCommand();
        updateCommand.setCommentId(updateCommentId);
        updateCommand.setContent(args);

        //Act & Assert
        mockMvc.perform(post(LOGIN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginCommand))
            )
            .andExpect(status().isOk())
            .andDo(
                result -> {
                    final var actions = mockMvc.perform(patch(UPDATE_COMMENT + updateCommentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCommand))
                    );

                    actions
                        .andDo(print())
                        .andExpect(status().isOk());
                });
    }
}
