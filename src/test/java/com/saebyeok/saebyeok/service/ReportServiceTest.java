package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportCategoryRepository;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.service.report.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "광고 게시물";
    private static final String CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";

    private ReportService reportService;

    @Mock
    private ReportCategoryRepository reportCategoryRepository;

    private ReportCategory reportCategory;

    @BeforeEach
    void setUp() {
        reportService = new ReportService(reportCategoryRepository);
        reportCategory = new ReportCategory(CATEGORY_ID, CATEGORY_NAME, CATEGORY_CONTENT);
    }

    @DisplayName("전체 ReportCategory를 조회하면 ReportCategory 목록이 반환된다")
    @Test
    void getReportCategories() {
        List<ReportCategory> reportCategories = new ArrayList<>();
        reportCategories.add(reportCategory);
        when(reportCategoryRepository.findAll()).thenReturn(reportCategories);

        List<ReportCategoryResponse> reportCategoryResponses = reportService.getReportCategories();

        assertThat(reportCategoryResponses).isNotNull();
        assertThat(reportCategoryResponses.get(0).getId()).isEqualTo(CATEGORY_ID);
        assertThat(reportCategoryResponses.get(0).getName()).isEqualTo(CATEGORY_NAME);
        assertThat(reportCategoryResponses.get(0).getContent()).isEqualTo(CATEGORY_CONTENT);
    }
}
