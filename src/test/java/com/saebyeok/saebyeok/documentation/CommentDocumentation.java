package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.dto.CommentResponse;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
        CommentCreateRequest commentCreateRequest = new CommentCreateRequest("내용", 1L, 2L);
        Comment comment = new Comment(1L, "내용", null, null, null, null, null, null, null);

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
                                fieldWithPath("articleId").type(JsonFieldType.NUMBER).description("댓글 게시물 ID"),
                                fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("상위 댓글 ID")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                        )
                ));
    }

    @Test
    void getComments() throws Exception {
        List<CommentResponse> comments = Arrays.asList(new CommentResponse(1L, "댓글1", "닉네임1",
                false, LocalDateTime.now(), true, 10L, false, true));

        given(commentService.getComment(any(), any())).willReturn(comments);

        this.mockMvc.perform(get("/api/articles/{articleId}/comments", ARTICLE_ID).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("comments/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("조회할 댓글의 목록을 포함하는 게시물의 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("댓글 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("댓글의 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글의 내용"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("댓글의 닉네임"),
                                fieldWithPath("[].isDeleted").type(JsonFieldType.BOOLEAN).description("댓글의 삭제 여부"),
                                fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("댓글의 작성 시간"),
                                fieldWithPath("[].isMine").type(JsonFieldType.BOOLEAN).description("댓글이 내가 쓴 댓글인지 여부"),
                                fieldWithPath("[].likesCount").type(JsonFieldType.NUMBER).description("댓글이 받은 공감의 수"),
                                fieldWithPath("[].isLikedByMe").type(JsonFieldType.BOOLEAN).description("댓글을 내가 공감한 상태인지 여부"),
                                fieldWithPath("[].hasNoParent").type(JsonFieldType.BOOLEAN).description("댓글이 상위 댓글인지 여부")
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
