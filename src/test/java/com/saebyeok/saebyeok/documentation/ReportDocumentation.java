package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.CommentReportCreateRequest;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
    void createCommentReport() throws Exception {
        CommentReportCreateRequest commentReportCreateRequest = new CommentReportCreateRequest("댓글 신고 내용", 1L, 1L);
        CommentReport commentReport = new CommentReport(1L, "댓글 신고 내용", null, null, null, LocalDateTime.now(), false);

        given(reportService.createCommentReport(any(Member.class), any())).willReturn(commentReport);

        String content = objectMapper.writeValueAsString(commentReportCreateRequest);

        this.mockMvc.perform(post("/api/reports/comment").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON).
                content(content).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("reports/comment/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 신고 내용"),
                                fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("신고할 댓글의 ID"),
                                fieldWithPath("reportCategoryId").type(JsonFieldType.NUMBER).description("신고의 분류 ID")),
                        responseHeaders(
                                headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                        )
                ));
    }
}
