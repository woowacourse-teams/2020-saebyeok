package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.CommentReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
public class ReportControllerTest {
    private static final String API = "/api";
    public static final String TEST_ARTICLE_REPORT_CONTENT = "게시물에 대한 신고 본문입니다.";
    public static final String TEST_COMMENT_REPORT_CONTENT = "댓글에 대한 신고 본문입니다.";
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "광고 게시물";
    private static final String TEST_CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";
    public static final Long TEST_ARTICLE_ID = 1L;
    public static final Long TEST_COMMENT_ID = 1L;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @MockBean
    private ReportService reportService;

    private MockMvc mockMvc;
    private ReportCategoryResponse reportCategoryResponse;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).
                apply(springSecurity()).
                build();

        this.reportCategoryResponse = new ReportCategoryResponse(TEST_CATEGORY_ID, TEST_CATEGORY_NAME, TEST_CATEGORY_CONTENT);
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

    @DisplayName("'/reports/comment'로 post 요청을 보내면 글을 생성한다")
    @Test
    void createCommentReportTest() throws Exception {
        CommentReportCreateRequest request = new CommentReportCreateRequest(TEST_COMMENT_REPORT_CONTENT, TEST_COMMENT_ID, TEST_CATEGORY_ID);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);
        CommentReport commentReport = new CommentReport(TEST_COMMENT_REPORT_CONTENT, null, null, null);

        when(reportService.createCommentReport(any(Member.class), any(CommentReportCreateRequest.class))).thenReturn(commentReport);

        this.mockMvc.perform(post(API + "/reports/comment").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }
}
