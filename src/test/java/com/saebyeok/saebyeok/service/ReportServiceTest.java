package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.dto.report.ReportCreateRequest;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
import com.saebyeok.saebyeok.exception.ReportTargetNotFoundException;
import com.saebyeok.saebyeok.service.report.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    public static final String REPORT_CONTENT = "이 게시물을 신고합니다";
    private static final Long MEMBER_ID = 1L;
    private static final String MEMBER_OAUTH_ID = "123456789";
    private static final String MEMBER_LOGIN_METHOD = "naver";
    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "광고 게시물";
    private static final String CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";
    private static final boolean IS_DELETED = false;
    private static final Long TARGET_CONTENT_ID = 1L;
    private static final Long INVALID_CATEGORY_ID = -1L;
    private static final String INVALID_REPORT_TARGET = "INVALID";

    private ReportService reportService;

    @Mock
    private ReportCategoryRepository reportCategoryRepository;

    @Mock
    private ReportRepository reportRepository;

    private ReportCategory reportCategory;
    private Member member;
    private Report report;

    @BeforeEach
    void setUp() {
        member = new Member(MEMBER_ID, MEMBER_OAUTH_ID, MEMBER_LOGIN_METHOD, LocalDateTime.now(), IS_DELETED, Role.USER, new ArrayList<>());

        reportService = new ReportService(reportCategoryRepository, reportRepository);
        reportCategory = new ReportCategory(CATEGORY_ID, CATEGORY_NAME, CATEGORY_CONTENT);

        report = new Report(REPORT_CONTENT, member, ReportTarget.ARTICLE, TARGET_CONTENT_ID, reportCategory);
    }

    @DisplayName("전체 ReportCategory를 조회하면 ReportCategory 목록이 반환된다")
    @Test
    void getReportCategoriesTest() {
        List<ReportCategory> reportCategories = new ArrayList<>();
        reportCategories.add(reportCategory);
        when(reportCategoryRepository.findAll()).thenReturn(reportCategories);

        List<ReportCategoryResponse> reportCategoryResponses = reportService.getReportCategories();

        assertThat(reportCategoryResponses).isNotNull();
        assertThat(reportCategoryResponses.get(0).getId()).isEqualTo(CATEGORY_ID);
        assertThat(reportCategoryResponses.get(0).getName()).isEqualTo(CATEGORY_NAME);
        assertThat(reportCategoryResponses.get(0).getContent()).isEqualTo(CATEGORY_CONTENT);
    }

    @DisplayName("게시물 신고 생성을 요청하면 게시물 신고가 생성된다")
    @Test
    void createReportTest() {
        ReportCreateRequest createRequest = new ReportCreateRequest(REPORT_CONTENT, TARGET_CONTENT_ID, CATEGORY_ID, ReportTarget.ARTICLE.getName());
        when(reportRepository.save(any(Report.class))).thenReturn(report);
        when(reportCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(reportCategory));

        Report createdReport = reportService.createReport(member, createRequest);

        assertThat(createdReport).
                isNotNull().
                isInstanceOf(Report.class);
    }

    @DisplayName("예외 테스트 : 잘못된 카테고리 ID로 게시글 신고를 생성하면 예외가 발생한다.")
    @Test
    void createReportWithCategoryExceptionTest() {
        ReportCreateRequest invalidRequest = new ReportCreateRequest(REPORT_CONTENT, TARGET_CONTENT_ID, INVALID_CATEGORY_ID, ReportTarget.ARTICLE.getName());
        when(reportCategoryRepository.findById(INVALID_CATEGORY_ID)).thenThrow(new ReportCategoryNotFoundException(INVALID_CATEGORY_ID));

        assertThatThrownBy(() -> reportService.createReport(member, invalidRequest)).
                isInstanceOf(ReportCategoryNotFoundException.class).
                hasMessage(INVALID_CATEGORY_ID + "에 해당하는 신고 분류를 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : 잘못된 카테고리 타겟을 포함한 Request로 게시글 신고를 생성하면 예외가 발생한다.")
    @Test
    void createReportWithTargetTextExceptionTest() {
        ReportCreateRequest invalidRequest = new ReportCreateRequest(REPORT_CONTENT, TARGET_CONTENT_ID, CATEGORY_ID, INVALID_REPORT_TARGET);
        when(reportCategoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(reportCategory));

        assertThatThrownBy(() -> reportService.createReport(member, invalidRequest)).
                isInstanceOf(ReportTargetNotFoundException.class).
                hasMessage(INVALID_REPORT_TARGET + "에 해당하는 신고 타입을 찾을 수 없습니다.");
    }
}
