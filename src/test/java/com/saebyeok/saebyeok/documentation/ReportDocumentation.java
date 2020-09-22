package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.dto.*;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.service.report.ReportService;
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
public class ReportDocumentation extends Documentation {
    protected TokenResponse tokenResponse;
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity())
                .build();
        tokenResponse = new TokenResponse("token", "bearer");
    }

    @Test
    void getReportCategories() throws Exception {
        List<ReportCategoryResponse> reportCategoryResponses = Arrays.asList(new ReportCategoryResponse(1L, "카테고리 이름", "카테고리 상세설명"));

        given(reportService.getReportCategories()).willReturn(reportCategoryResponses);

        this.mockMvc.perform(get("/api/reports/categories").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("reports/categories",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("조회할 신고 분류의 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("조회할 신고 분류의 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("조회할 신고 분류의 이름"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("조회할 신고 분류의 상세 설명")
                        )
                ));
    }

    @Test
    void createArticleReport() throws Exception {
        ArticleReportCreateRequest articleReportCreateRequest = new ArticleReportCreateRequest("게시물 신고 내용", 1L, 1L);
        ArticleReport articleReport = new ArticleReport(1L, "게시물 신고 내용", null, null, null, LocalDateTime.now(), false);

        given(reportService.createArticleReport(any(Member.class), any())).willReturn(articleReport);

        String content = objectMapper.writeValueAsString(articleReportCreateRequest);

        this.mockMvc.perform(post("/api/reports/article").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON).
                content(content).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("reports/article/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 신고 내용"),
                                fieldWithPath("articleId").type(JsonFieldType.NUMBER).description("신고할 게시물의 ID"),
                                fieldWithPath("reportCategoryId").type(JsonFieldType.NUMBER).description("신고의 분류 ID")),
                        responseHeaders(
                                headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                        )
                ));
    }

    @Test
    void getArticleReports() throws Exception {
        ArticleReport articleReport = new ArticleReport(1L, "게시물 신고 내용", null, null, new ReportCategory(1L, "신고 분류 이름", "신고 분류 상세 내용"), LocalDateTime.now(), false);

        List<CommentResponse> comments = Arrays.asList(new CommentResponse(1L, "댓글1", "닉네임1", false,
                LocalDateTime.now(), true, 10L, false));
        EmotionResponse emotionResponse = new EmotionResponse(1L, "기뻐요", "이미지 리소스 링크");
        List<SubEmotionResponse> subEmotionResponses = Arrays.asList(new SubEmotionResponse(1L, "행복해요"), new SubEmotionResponse(2L, "설레요"));
        ArticleResponse articleResponse = new ArticleResponse(1L, "내용", LocalDateTime.now(), emotionResponse, subEmotionResponses, true, true, 10L, false, comments);

        List<ArticleReportResponse> articleReportResponses = Arrays.asList(new ArticleReportResponse(articleReport, articleResponse));

        given(reportService.getArticleReports(any(Member.class))).willReturn(articleReportResponses);

        this.mockMvc.perform(get("/api/reports/article").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("reports/article/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("전체 게시물 신고의 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("게시물 신고의 ID"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("게시물 신고의 내용"),
                                fieldWithPath("[].article").type(JsonFieldType.OBJECT).description("신고된 게시물"),
                                fieldWithPath("[].reportCategory").type(JsonFieldType.OBJECT).description("신고의 분류"),
                                fieldWithPath("[].isFinished").type(JsonFieldType.BOOLEAN).description("신고의 완료 여부"),
                                fieldWithPath("[].article.id").type(JsonFieldType.NUMBER).description("신고된 게시물의 ID"),
                                fieldWithPath("[].article.content").type(JsonFieldType.STRING).description("신고된 게시물의 내용"),
                                fieldWithPath("[].article.createdDate").type(JsonFieldType.STRING).description("신고된 게시물의 작성 시간"),
                                subsectionWithPath("[].article.emotion").type(JsonFieldType.OBJECT).description("신고된 게시물의 감정 대분류"),
                                subsectionWithPath("[].article.subEmotions[]").type(JsonFieldType.ARRAY).description("신고된 게시글의 감정 소분류 목록"),
                                fieldWithPath("[].article.isCommentAllowed").type(JsonFieldType.BOOLEAN).description("신고된 게시글의 댓글 허용 여부"),
                                fieldWithPath("[].article.isMine").type(JsonFieldType.BOOLEAN).description("신고된 게시글이 내가 쓴 글인지 여부"),
                                fieldWithPath("[].article.likesCount").type(JsonFieldType.NUMBER).description("신고된 게시글이 받은 공감의 수"),
                                fieldWithPath("[].article.isLikedByMe").type(JsonFieldType.BOOLEAN).description("신고된 게시글을 내가 공감한 상태인지 여부"),
                                subsectionWithPath("[].article.comments[]").type(JsonFieldType.ARRAY).description("신고된 게시물의 댓글 목록"),
                                fieldWithPath("[].reportCategory.id").type(JsonFieldType.NUMBER).description("신고 분류의 ID"),
                                fieldWithPath("[].reportCategory.name").type(JsonFieldType.STRING).description("신고 분류의 이름"),
                                fieldWithPath("[].reportCategory.content").type(JsonFieldType.STRING).description("신고 분류의 상세 설명")
                        )
                ));
    }

    @Test
    void readArticleReports() throws Exception {
        ArticleReport articleReport = new ArticleReport(1L, "게시물 신고 내용", null, null, new ReportCategory(1L, "신고 분류 이름", "신고 분류 상세 내용"), LocalDateTime.now(), false);

        List<CommentResponse> comments = Arrays.asList(new CommentResponse(1L, "댓글1", "닉네임1", false,
                LocalDateTime.now(), true, 10L, false));
        EmotionResponse emotionResponse = new EmotionResponse(1L, "기뻐요", "이미지 리소스 링크");
        List<SubEmotionResponse> subEmotionResponses = Arrays.asList(new SubEmotionResponse(1L, "행복해요"), new SubEmotionResponse(2L, "설레요"));
        ArticleResponse articleResponse = new ArticleResponse(1L, "내용", LocalDateTime.now(), emotionResponse, subEmotionResponses, true, true, 10L, false, comments);

        ArticleReportResponse articleReportResponse = new ArticleReportResponse(articleReport, articleResponse);

        given(reportService.readArticleReport(any(Member.class), any())).willReturn(articleReportResponse);

        this.mockMvc.perform(get("/api/reports/article/{reportId}", 1L).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andDo(print()).
                andDo(document("reports/article/read",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        pathParameters(
                                parameterWithName("reportId").description("삭제할 게시물 신고의 게시물 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시물 신고의 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시물 신고의 내용"),
                                fieldWithPath("article").type(JsonFieldType.OBJECT).description("신고된 게시물"),
                                fieldWithPath("reportCategory").type(JsonFieldType.OBJECT).description("신고의 분류"),
                                fieldWithPath("isFinished").type(JsonFieldType.BOOLEAN).description("신고의 완료 여부"),
                                fieldWithPath("article.id").type(JsonFieldType.NUMBER).description("신고된 게시물의 ID"),
                                fieldWithPath("article.content").type(JsonFieldType.STRING).description("신고된 게시물의 내용"),
                                fieldWithPath("article.createdDate").type(JsonFieldType.STRING).description("신고된 게시물의 작성 시간"),
                                subsectionWithPath("article.emotion").type(JsonFieldType.OBJECT).description("신고된 게시물의 감정 대분류"),
                                subsectionWithPath("article.subEmotions[]").type(JsonFieldType.ARRAY).description("신고된 게시글의 감정 소분류 목록"),
                                fieldWithPath("article.isCommentAllowed").type(JsonFieldType.BOOLEAN).description("신고된 게시글의 댓글 허용 여부"),
                                fieldWithPath("article.isMine").type(JsonFieldType.BOOLEAN).description("신고된 게시글이 내가 쓴 글인지 여부"),
                                fieldWithPath("article.likesCount").type(JsonFieldType.NUMBER).description("신고된 게시글이 받은 공감의 수"),
                                fieldWithPath("article.isLikedByMe").type(JsonFieldType.BOOLEAN).description("신고된 게시글을 내가 공감한 상태인지 여부"),
                                subsectionWithPath("article.comments[]").type(JsonFieldType.ARRAY).description("신고된 게시물의 댓글 목록"),
                                fieldWithPath("reportCategory.id").type(JsonFieldType.NUMBER).description("신고 분류의 ID"),
                                fieldWithPath("reportCategory.name").type(JsonFieldType.STRING).description("신고 분류의 이름"),
                                fieldWithPath("reportCategory.content").type(JsonFieldType.STRING).description("신고 분류의 상세 설명")
                        )
                ));
    }

    @Test
    void deleteArticleReport() throws Exception {
        this.mockMvc.perform(delete("/api/reports/article/{reportId}", 1L).
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent()).
                andDo(print()).
                andDo(document("reports/article/delete",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")),
                        pathParameters(
                                parameterWithName("reportId").description("삭제할 게시물 신고의 게시물 ID")
                        ))
                );
    }
}
