package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.Report;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.dto.report.ReportCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
import com.saebyeok.saebyeok.exception.ReportTargetNotFoundException;
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
    // TODO: 2020/10/05 ReportController에 신고 조회 로직 없이 신고 생성 로직만 있어서, 부득이하게 신고에 대한 인수테스트를 만들지 않았음. 추후 기능 추가시 작성해보자.
    public static final String TEST_REPORT_CONTENT = "신고 본문입니다.";
    public static final Long TEST_TARGET_CONTENT_ID = 1L;
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "광고 게시물";
    private static final String TEST_CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";
    private static final String API = "/api";
    private static final String TEST_REPORT_TARGET = "ARTICLE";

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

    @DisplayName("'/reports'로 post 요청을 보내면 신고를 생성한다")
    @Test
    void createReportTest() throws Exception {
        ReportCreateRequest request = new ReportCreateRequest(TEST_REPORT_CONTENT, TEST_TARGET_CONTENT_ID, TEST_CATEGORY_ID, TEST_REPORT_TARGET);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);
        Report report = request.toReport(new Member(), new ReportCategory());

        when(reportService.createReport(any(Member.class), any(ReportCreateRequest.class))).thenReturn(report);

        this.mockMvc.perform(post(API + "/reports").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트 : 게시글 신고 생성 요청에 함께 전달된 Id가 존재하지 않을 경우, 예외가 발생한다")
    @Test
    void noExistArticleIdTest() throws Exception {
        when(reportService.createReport(any(Member.class), any(ReportCreateRequest.class))).thenThrow(ArticleNotFoundException.class);

        ReportCreateRequest request = new ReportCreateRequest(TEST_REPORT_CONTENT, null, TEST_CATEGORY_ID, TEST_REPORT_TARGET);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/reports").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }

    @DisplayName("예외 테스트 : 신고 생성 요청에 함께 전달된 reportCategoryId가 존재하지 않을 경우, 예외가 발생한다")
    @Test
    void noExistCategoryIdTest() throws Exception {
        when(reportService.createReport(any(Member.class), any(ReportCreateRequest.class))).thenThrow(ReportCategoryNotFoundException.class);

        ReportCreateRequest request = new ReportCreateRequest(TEST_REPORT_CONTENT, TEST_TARGET_CONTENT_ID, null, TEST_REPORT_TARGET);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/reports").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }

    @DisplayName("예외 테스트 : 신고 생성 요청에 함께 전달된 타입 문자열이 존재하지 않을 경우, 예외가 발생한다")
    @Test
    void noExistTypeStringTest() throws Exception {
        when(reportService.createReport(any(Member.class), any(ReportCreateRequest.class))).thenThrow(ReportTargetNotFoundException.class);

        ReportCreateRequest request = new ReportCreateRequest(TEST_REPORT_CONTENT, TEST_TARGET_CONTENT_ID, TEST_CATEGORY_ID, null);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/reports").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }
}
