package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
class CommentDocumentation extends Documentation {
    private static final Long COMMENT_ID = 1L;
    private static final Long ARTICLE_ID = 1L;

    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void createComment() throws Exception {
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("내용", 1L);
        Comment comment = commentCreateRequest.toComment();

        given(commentService.createComment(any(), any())).willReturn(comment);

        String content = objectMapper.writeValueAsString(commentCreateRequest);

        this.mockMvc.perform(post("/api/articles/{articleId}/comments", ARTICLE_ID).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON).
                content(content).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("comments/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("articleId").description("댓글 게시물 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("articleId").type(JsonFieldType.NUMBER).description("댓글 게시물 ID")),
                        responseHeaders(
                                headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                        )
                ));
    }

    @Test
    void deleteComment() throws Exception {
        this.mockMvc.perform(delete("/api/articles/{articleId}/comments/{commentId}", ARTICLE_ID, COMMENT_ID).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent()).
                andDo(print()).
                andDo(document("comments/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        pathParameters(
                                parameterWithName("articleId").description("삭제할 댓글의 게시물 ID"),
                                parameterWithName("commentId").description("삭제할 댓글의 ID"))
                ));
    }
}
