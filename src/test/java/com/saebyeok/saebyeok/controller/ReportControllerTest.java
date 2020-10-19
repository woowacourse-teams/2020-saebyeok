package com.saebyeok.saebyeok.controller;

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
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
class ReportControllerTest {
    private static final String API = "/api";
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "광고 게시물";
    private static final String TEST_CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";

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
        when(reportService.getReportCategories()).thenReturn(Arrays.asList(this.reportCategoryResponse));

        this.mockMvc.perform(get(API + "/reports/categories").
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].name").value(TEST_CATEGORY_NAME));
    }
}
