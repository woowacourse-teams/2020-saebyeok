package com.saebyeok.saebyeok.documentation;

import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.ArticleLike;
import com.saebyeok.saebyeok.domain.CommentLike;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
public class LikeDocumentation extends Documentation {
    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void likeArticle() throws Exception {
        Long articleId = 1L;

        given(likeService.likeArticle(any(Member.class), eq(articleId))).willReturn(new ArticleLike());

        this.mockMvc.perform(post("/api/likes/article/{articleId}", articleId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("likes/article",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("articleId").description("공감하는 게시물의 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("새로 생성된 공감 자원의 위치")
                        )
                ));
    }

    @Test
    void unlikeArticle() throws Exception {
        Long articleId = 1L;

        doNothing().when(likeService).unlikeArticle(any(Member.class), eq(articleId));

        this.mockMvc.perform(delete("/api/likes/article/{articleId}", articleId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent()).
                andDo(print()).
                andDo(document("unlikes/article",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("articleId").description("공감을 취소하는 게시물의 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        )
                ));
    }

    @Test
    void likeComment() throws Exception {
        Long commentId = 1L;

        given(likeService.likeComment(any(Member.class), eq(commentId))).willReturn(new CommentLike());

        this.mockMvc.perform(post("/api/likes/comment/{commentId}", commentId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("likes/comment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("commentId").description("공감하는 댓글의 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("새로 생성된 공감 자원의 위치")
                        )
                ));
    }

    @Test
    void unlikeComment() throws Exception {
        Long commentId = 1L;

        doNothing().when(likeService).unlikeComment(any(Member.class), eq(commentId));

        this.mockMvc.perform(delete("/api/likes/comment/{commentId}", commentId).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent()).
                andDo(print()).
                andDo(document("unlikes/comment",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("commentId").description("공감을 취소하는 댓글의 ID")),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        )
                ));
    }
}
