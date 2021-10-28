package com.yam.app.extension;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.presentation.AccountCommandApi;
import com.yam.app.account.presentation.AccountQueryApi;
import com.yam.app.article.application.ArticleFacade;
import com.yam.app.article.presentation.ArticleCommandApi;
import com.yam.app.comment.application.CommentFacade;
import com.yam.app.comment.presentation.CommentCommandApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@WebMvcTest(
    value = {
        AccountCommandApi.class,
        AccountQueryApi.class,
        ArticleCommandApi.class,
        CommentCommandApi.class
    }
)
public class WebApiTestExtension {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected AccountFacade accountFacade;
    @MockBean
    protected CommentFacade commentFacade;
    @MockBean
    protected ArticleFacade articleFacade;

    protected void assertThatInvalidArgumentError(ResultActions actions) throws Exception {
        actions
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andExpect(jsonPath("$.message").value("Invalid argument"));
    }
}
