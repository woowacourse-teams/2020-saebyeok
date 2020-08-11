package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.controller.ArticleController;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ArticleController.class})
public class ArticleDocumentation extends Documentation {
    private static final Long ARTICLE_ID = 1L;
    protected TokenResponse tokenResponse;

    @Autowired
    private ArticleController articleController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        super.setUp(context, restDocumentation);
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void createArticle() throws Exception {
        ArticleCreateRequest articleCreateRequest = new ArticleCreateRequest("내용", "기뻐요", true);
        Article article = articleCreateRequest.toArticle();

        given(articleService.createArticle(any(), any())).willReturn(article);

        String content = objectMapper.writeValueAsString(articleCreateRequest);

        this.mockMvc.perform(post("/api/articles").
            header("Authorization", tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON).
            content(content).
            contentType(MediaType.APPLICATION_JSON)).
            andExpect(status().isCreated()).
            andDo(print()).
            andDo(document("articles/create",
                           getDocumentRequest(),
                           getDocumentResponse(),
                           requestHeaders(
                               headerWithName("Authorization").description("Bearer auth credentials")
                           ),
                           requestFields(
                               fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 내용"),
                               fieldWithPath("emotion").type(JsonFieldType.STRING).description("게시물 감정"),
                               fieldWithPath("isCommentAllowed").type(JsonFieldType.BOOLEAN).description("게시물의 댓글 허용 " +
                                                                                                             "여부")),
                           responseHeaders(
                               headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                           )
            ));
    }

    @Test
    void readArticle() throws Exception {
        List<CommentResponse> comments = Arrays.asList(new CommentResponse(1L, "댓글1", "닉네임1", false,
                                                                           LocalDateTime.now()));
        ArticleResponse articleResponse = new ArticleResponse(ARTICLE_ID, "내용", LocalDateTime.now(), "기뻐요", true,
                                                              comments);

        given(articleService.readArticle(any())).willReturn(articleResponse);

        this.mockMvc.perform(get("/api/articles/{articleId}", ARTICLE_ID).
            header("Authorization", tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON)).
            andExpect(status().isOk()).
            andDo(print()).
            andDo(document("articles/read",
                           getDocumentRequest(),
                           getDocumentResponse(),
                           requestHeaders(
                               headerWithName("Authorization").description("Bearer auth credentials")
                           ),
                           pathParameters(
                               parameterWithName("articleId").description("조회할 게시물의 ID")
                           ),
                           responseFields(
                               fieldWithPath("id").type(JsonFieldType.NUMBER).description("조회할 게시물의 ID"),
                               fieldWithPath("content").type(JsonFieldType.STRING).description("조회할 게시물의 내용"),
                               fieldWithPath("createdDate").type(JsonFieldType.STRING).description("조회할 게시물의 작성 시간"),
                               fieldWithPath("emotion").type(JsonFieldType.STRING).description("조회할 게시물의 감정"),
                               fieldWithPath("isCommentAllowed").type(JsonFieldType.BOOLEAN).description("조회할 게시물의 댓글" +
                                                                                                             " 허용 여부"),
                               fieldWithPath("comments[]").type(JsonFieldType.ARRAY).description("조회할 게시물의 댓글 목록"),
                               fieldWithPath("comments[].id").type(JsonFieldType.NUMBER).description("조회할 게시물의 댓글의 ID"),
                               fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("조회할 게시물의 " +
                                                                                                              "댓글의 내용"),
                               fieldWithPath("comments[].nickname").type(JsonFieldType.STRING).description("조회할 게시물의 " +
                                                                                                               "댓글의 " +
                                                                                                               "닉네임"),
                               fieldWithPath("comments[].isDeleted").type(JsonFieldType.BOOLEAN).description("조회할 게시물의 댓글의 삭제 여부"),
                               fieldWithPath("comments[].createdDate").type(JsonFieldType.STRING).description("조회할 게시물의 댓글의 작성 시간")
                           )
            ));
    }

    @Test
    void getArticles() throws Exception {
        List<CommentResponse> comments = Arrays.asList(new CommentResponse(1L, "댓글1", "닉네임1", false,
                                                                           LocalDateTime.now()));
        ArticleResponse articleResponse = new ArticleResponse(ARTICLE_ID, "내용", LocalDateTime.now(), "기뻐요", true,
                                                              comments);

        List<ArticleResponse> articleResponses = Arrays.asList(articleResponse);

        given(articleService.getArticles(anyInt(), anyInt())).willReturn(articleResponses);

        this.mockMvc.perform(get("/api/articles?page=0&size=5").
            header("Authorization", tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON)).
            andExpect(status().isOk()).
            andDo(print()).
            andDo(document("articles/get",
                           getDocumentRequest(),
                           getDocumentResponse(),
                           requestHeaders(
                               headerWithName("Authorization").description("Bearer auth credentials")
                           ),
                           requestParameters(
                               parameterWithName("page").description("확인할 게시물의 페이지"),
                               parameterWithName("size").description("확인할 게시물의 개수")
                           ),
                           responseFields(
                               fieldWithPath("[]").type(JsonFieldType.ARRAY).description("전체 게시물의 목록"),
                               fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물의 ID"),
                               fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시물의 내용"),
                               fieldWithPath("[].createdDate").type(JsonFieldType.STRING).description("게시물의 작성 시간"),
                               fieldWithPath("[].emotion").type(JsonFieldType.STRING).description("게시물의 감정"),
                               fieldWithPath("[].isCommentAllowed").type(JsonFieldType.BOOLEAN).description("게시물의 댓글" +
                                                                                                                " 허용 " +
                                                                                                                "여부"),
                               fieldWithPath("[].comments[]").type(JsonFieldType.ARRAY).description("조회할 게시물의 댓글 목록"),
                               fieldWithPath("[].comments[].id").type(JsonFieldType.NUMBER).description("조회할 게시물의 댓글의" +
                                                                                                            " ID"),
                               fieldWithPath("[].comments[].content").type(JsonFieldType.STRING).description("조회할 게시물의 " +
                                                                                                                 "댓글의 내용"),
                               fieldWithPath("[].comments[].nickname").type(JsonFieldType.STRING).description("조회할 게시물의 " +
                                                                                                                  "댓글의 " +
                                                                                                                  "닉네임"),
                               fieldWithPath("[].comments[].isDeleted").type(JsonFieldType.BOOLEAN).description("조회할 게시물의 댓글의 삭제 여부"),
                               fieldWithPath("[].comments[].createdDate").type(JsonFieldType.STRING).description("조회할 게시물의 댓글의 작성 시간")
                           )
            ));
    }

    @Test
    void deleteArticle() throws Exception {
        this.mockMvc.perform(delete("/api/articles/{articleId}", ARTICLE_ID).
            header("Authorization", tokenResponse.getAccessToken()).
            accept(MediaType.APPLICATION_JSON)).
            andExpect(status().isNoContent()).
            andDo(print()).
            andDo(document("articles/delete",
                           getDocumentRequest(),
                           getDocumentResponse(),
                           requestHeaders(
                               headerWithName("Authorization").description("Bearer auth credentials")),
                           pathParameters(
                               parameterWithName("articleId").description("삭제할 댓글의 게시물 ID")
                           ))
            );
    }
}
