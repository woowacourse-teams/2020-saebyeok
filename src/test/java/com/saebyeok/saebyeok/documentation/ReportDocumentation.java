package com.saebyeok.saebyeok.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.documentation.common.Documentation;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.Report;
import com.saebyeok.saebyeok.domain.report.ReportTarget;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.dto.report.ReportCreateRequest;
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
        ReportCreateRequest reportCreateRequest = new ReportCreateRequest("게시물 신고 내용", 1L, 1L, "Article");
        Report report = new Report("게시물에 대한 신고입니다.", null, ReportTarget.ARTICLE, 1L, null);

        given(reportService.createReport(any(Member.class), any())).willReturn(report);

        String content = objectMapper.writeValueAsString(reportCreateRequest);

        this.mockMvc.perform(post("/api/reports").
                header("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken()).
                accept(MediaType.APPLICATION_JSON).
                content(content).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andDo(document("reports/create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer auth credentials")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("신고 내용"),
                                fieldWithPath("targetContentId").type(JsonFieldType.NUMBER).description("신고할 게시물 혹은 댓글의 ID"),
                                fieldWithPath("reportCategoryId").type(JsonFieldType.NUMBER).description("신고의 카테고리 ID"),
                                fieldWithPath("reportTarget").type(JsonFieldType.STRING).description("신고 대상의 분류. 게시글 혹은 댓글로 분류되고 있음.")
                        ),
                        responseHeaders(
                                headerWithName("Location").description("생성 성공 시 해당 주소로 이동")
                        )
                ));
    }
}
