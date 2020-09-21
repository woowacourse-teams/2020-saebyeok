package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.exception.ReportNotFoundException;
import com.saebyeok.saebyeok.service.report.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
public class ReportControllerTest {
    private static final String API = "/api";
    public static final String TEST_ARTICLE_REPORT_CONTENT = "게시물에 대한 신고 본문입니다.";
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "광고 게시물";
    private static final String TEST_CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";
    public static final Long TEST_ARTICLE_ID = 1L;
    public static final Long TEST_ARTICLE_REPORT_ID_1 = 1L;
    public static final Long TEST_ARTICLE_REPORT_ID_2 = 2L;
    public static final Long TEST_INVALID_REPORT_ID = 10000L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private ReportService reportService;

    private MockMvc mockMvc;
    private ReportCategoryResponse reportCategoryResponse;
    private List<ArticleReportResponse> articleReportResponses;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).
                apply(springSecurity()).
                build();

        this.reportCategoryResponse = new ReportCategoryResponse(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_CATEGORY_CONTENT);

        articleReportResponses = new ArrayList<>();
        articleReportResponses.add(new ArticleReportResponse(TEST_ARTICLE_REPORT_ID_1, TEST_ARTICLE_REPORT_CONTENT, TEST_ARTICLE_ID, reportCategoryResponse, false));
        articleReportResponses.add(new ArticleReportResponse(TEST_ARTICLE_REPORT_ID_2, TEST_ARTICLE_REPORT_CONTENT, TEST_ARTICLE_ID, reportCategoryResponse, false));
    }

    @DisplayName("'/reports/categories'로 get 요청을 보내면 ReportCategory 리스트를 받는다.")
    @Test
    void getReportCategoriesTest() throws Exception {
        when(reportService.getReportCategories()).thenReturn(Arrays.asList(reportCategoryResponse));

        this.mockMvc.perform(get(API + "/reports/categories").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].name").value(TEST_CATEGORY_NAME));
    }

    @DisplayName("'/reports/article'로 post 요청을 보내면 글을 생성한다")
    @Test
    void createArticleReportTest() throws Exception {
        ArticleReportCreateRequest request = new ArticleReportCreateRequest(TEST_ARTICLE_REPORT_CONTENT, TEST_ARTICLE_ID, TEST_CATEGORY_ID);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);
        ArticleReport articleReport = new ArticleReport(TEST_ARTICLE_REPORT_CONTENT, null, null, null);

        when(reportService.createArticleReport(any(Member.class), any(ArticleReportCreateRequest.class))).thenReturn(articleReport);

        this.mockMvc.perform(post(API + "/reports/article").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("'/reports/article'로 get 요청을 보내면 신고 목록을 반환한다.")
    @Test
    void getArticleReportsTest() throws Exception {
        when(reportService.getArticleReports(any(Member.class))).thenReturn(articleReportResponses);

        this.mockMvc.perform(get(API + "/reports/article").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(2))).
                andExpect(jsonPath("$[0].id").value(TEST_ARTICLE_REPORT_ID_1)).
                andExpect(jsonPath("$[0].content").value(TEST_ARTICLE_REPORT_CONTENT)).
                andExpect(jsonPath("$[0].articleId").value(TEST_ARTICLE_ID)).
                andExpect(jsonPath("$[1].id").value(TEST_ARTICLE_REPORT_ID_2)).
                andExpect(jsonPath("$[1].content").value(TEST_ARTICLE_REPORT_CONTENT)).
                andExpect(jsonPath("$[1].articleId").value(TEST_ARTICLE_ID));
    }

    @DisplayName("ID로 개별 신고 조회를 요청하면 해당 신고를 반환한다.")
    @Test
    void readArticleReportTest() throws Exception {
        when(reportService.readArticleReport(any(Member.class), eq(TEST_ARTICLE_REPORT_ID_1))).thenReturn(articleReportResponses.get(0));

        this.mockMvc.perform(get(API + "/reports/article/" + TEST_ARTICLE_REPORT_ID_1).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(TEST_ARTICLE_REPORT_ID_1)).
                andExpect(jsonPath("$.content").value(TEST_ARTICLE_REPORT_CONTENT)).
                andExpect(jsonPath("$.articleId").value(TEST_ARTICLE_ID)).
                andExpect(jsonPath("$.isFinished").value(false));
    }

    @DisplayName("에외 테스트 : 없는 ID로 신고 조회를 요청하면, ReportNotFoundException이 발생한다.")
    @Test
    void readArticleReportExceptionTest() throws Exception {
        when(reportService.readArticleReport(any(Member.class), eq(TEST_INVALID_REPORT_ID))).
                thenThrow(ReportNotFoundException.class);

        this.mockMvc.perform(get(API + "/reports/article/" + TEST_INVALID_REPORT_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("ID로 신고 삭제를 요청하면 해당 신고의 flag를 조정해 soft delete 처리한다.")
    @Test
    void deleteArticleReportTest() throws Exception {
        doNothing().when(reportService).deleteArticleReport(any(Member.class), eq(TEST_ARTICLE_REPORT_ID_1));

        this.mockMvc.perform(delete(API + "/reports/article/" + TEST_ARTICLE_REPORT_ID_1)).
                andExpect(status().isNoContent());
    }

    @DisplayName("예외 테스트 : 없는 ID로 신고 삭제를 요청하면 ReportNotFoundException이 발생한다.")
    @Test
    void deleteArticleReportExceptionTest() throws Exception {
        doThrow(new ReportNotFoundException(TEST_INVALID_REPORT_ID)).
                when(reportService).deleteArticleReport(any(Member.class), eq(TEST_INVALID_REPORT_ID));

        this.mockMvc.perform(delete(API + "/reports/article/" + TEST_INVALID_REPORT_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }
}
