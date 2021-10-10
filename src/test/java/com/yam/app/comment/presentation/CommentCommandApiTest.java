package com.yam.app.comment.presentation;

import static com.yam.app.comment.presentation.CommentApiUri.CREATE_COMMENT;
import static com.yam.app.comment.presentation.CommentApiUri.DELETE_COMMENT;
import static com.yam.app.comment.presentation.CommentApiUri.UPDATE_COMMENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.comment.application.CommentFacade;
import java.util.Random;
import org.javaunit.autoparams.AutoSource;
import org.javaunit.autoparams.customization.Customization;
import org.javaunit.autoparams.customization.SettablePropertyWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("Comment Command HTTP API")
@WebMvcTest(CommentCommandApi.class)
final class CommentCommandApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CommentFacade commentFacade;

    private String generatedRandomString(int length) {
        Random random = new Random();

        return random.ints(48, 123)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    @Nested
    @DisplayName("댓글 작성 HTTP API")
    class CreateCommentApi {

        @ParameterizedTest
        @AutoSource
        @Customization(SettablePropertyWriter.class)
        @DisplayName("인증되지 않은 사용자가 요청을 보냈다면 401에러를 반환한다.")
        void unauthenticated_user_request(CreateCommentCommand command) throws Exception {
            //Act
            final var actions = mockMvc.perform(post(CREATE_COMMENT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
        @DisplayName("인증된 사용자의 요청 바디의 content 가 비어있거나 null 이라면 400에러를 반환한다.")
        void authenticated_user_request_body_content_is_empty_or_null(String args)
            throws Exception {
            //Arrange
            var session = new MockHttpSession();
            var command = new CreateCommentCommand();
            command.setContent(args);
            command.setArticleId(0L);

            //Act
            final var actions = mockMvc.perform(post(CREATE_COMMENT)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증된 사용자의 요청 바디의 articleId 가 null 이라면 400에러를 반환한다.")
        void authenticated_user_request_body_articleId_is_null(String args) throws Exception {
            //Arrange
            var session = new MockHttpSession();
            var command = new CreateCommentCommand();
            command.setContent(args);
            command.setArticleId(null);

            //Act
            final var actions = mockMvc.perform(post(CREATE_COMMENT)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증된 사용자의 요청 바디의 content 가 최대 Length 를 초과한 경우 400에러를 반환한다.")
        void authentication_user_request_body_has_exceeded_content_length_limit(Long args)
            throws Exception {
            //Arrange
            var maxContentLength = 120;
            var session = new MockHttpSession();
            var exceededLengthCommand = new CreateCommentCommand();
            exceededLengthCommand.setContent(generatedRandomString(maxContentLength + 1));
            exceededLengthCommand.setArticleId(args);

            //Act
            final var actions = mockMvc.perform(post(CREATE_COMMENT)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exceededLengthCommand))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));

        }
    }

    @Nested
    @DisplayName("댓글 수정 HTTP API")
    class UpdateCommentApi {

        @ParameterizedTest
        @AutoSource
        @Customization(SettablePropertyWriter.class)
        @DisplayName("인증되지 않은 사용자가 요청을 보냈다면 401에러를 반환한다.")
        void unauthenticated_user_request(UpdateCommentCommand command) throws Exception {
            //Act
            final var actions = mockMvc.perform(patch(UPDATE_COMMENT + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
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
        @DisplayName("인증된 사용자의 요청 바디의 content 가 비어있거나 null 이라면 400에러를 반환한다.")
        void authenticated_user_request_body_content_is_empty_or_null(String args)
            throws Exception {
            //Arrange
            var session = new MockHttpSession();
            var command = new UpdateCommentCommand();
            command.setContent(args);
            command.setCommentId(1L);

            //Act
            final var actions = mockMvc.perform(patch(UPDATE_COMMENT + "1")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증된 사용자의 요청 바디의 commentId 가 null 이라면 400에러를 반환한다.")
        void authenticated_user_request_body_articleId_is_null(String args) throws Exception {
            //Arrange
            var session = new MockHttpSession();
            var command = new UpdateCommentCommand();
            command.setContent(args);
            command.setCommentId(null);

            //Act
            final var actions = mockMvc.perform(patch(UPDATE_COMMENT + "1")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("인증된 사용자의 요청 바디의 content 가 최대 Length 를 초과한 경우 400에러를 반환한다.")
        void authentication_user_request_body_has_exceeded_content_length_limit(Long args)
            throws Exception {
            //Arrange
            var maxContentLength = 120;
            var session = new MockHttpSession();
            var exceededLengthCommand = new UpdateCommentCommand();
            exceededLengthCommand.setContent(generatedRandomString(maxContentLength + 1));
            exceededLengthCommand.setCommentId(args);

            //Act
            final var actions = mockMvc.perform(patch(UPDATE_COMMENT + "1")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exceededLengthCommand))
            );

            //Assert
            actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.message").value("Invalid argument"));

        }
    }

    @Nested
    @DisplayName("댓글 삭제 HTTP API")
    class DeleteCommentApi {

        @Test
        @DisplayName("인증되지 않은 사용자가 요청을 보냈다면 401에러를 반환한다.")
        void unauthenticated_user_request() throws Exception {
            //Act
            final var actions = mockMvc.perform(delete(DELETE_COMMENT + "1")
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

}
