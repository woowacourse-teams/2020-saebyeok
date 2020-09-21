package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.exception.ReportNotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    public static final String ARTICLE_REPORT_CONTENT = "이 게시물을 신고합니다";
    private static final Long MEMBER_ID = 1L;
    private static final String MEMBER_OAUTH_ID = "123456789";
    private static final String MEMBER_LOGIN_METHOD = "naver";
    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "광고 게시물";
    private static final String CATEGORY_CONTENT = "상업적 목적을 가진 게시물에 해당합니다.";
    private static final boolean IS_DELETED = false;
    private static final Long ARTICLE_ID = 1L;
    private static final Long INVALID_REPORT_ID = 10000L;
    private static final Long ARTICLE_REPORT_ID = 1L;

    private ReportService reportService;
    @Mock
    private ArticleService articleService;
    @Mock
    private CommentService commentService;
    @Mock
    private ReportCategoryRepository reportCategoryRepository;
    @Mock
    private ArticleReportRepository articleReportRepository;
    @Mock
    private CommentReportRepository commentReportRepository;

    private ReportCategory reportCategory;
    private Member member;
    private ArticleReport articleReport;

    @BeforeEach
    void setUp() {
        member = new Member(MEMBER_ID, MEMBER_OAUTH_ID, MEMBER_LOGIN_METHOD, LocalDateTime.now(), IS_DELETED, Role.USER, new ArrayList<>());
        Article article = new Article(ARTICLE_ID, "게시물", member, LocalDateTime.now(), false, false, null, new ArrayList<>());

        reportService = new ReportService(articleService, commentService, reportCategoryRepository, articleReportRepository, commentReportRepository);
        reportCategory = new ReportCategory(CATEGORY_ID, CATEGORY_NAME, CATEGORY_CONTENT);

        articleReport = new ArticleReport(ARTICLE_ID, ARTICLE_REPORT_CONTENT, member, article, reportCategory, LocalDateTime.now(), false);
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

    @DisplayName("전체 게시물 신고를 조회하면 게시물 신고 목록이 반환된다")
    @Test
    void getArticleReportsTest() {
        List<ArticleReport> articleReports = Arrays.asList(articleReport);
        when(articleReportRepository.findAll()).thenReturn(articleReports);

        List<ArticleReportResponse> articleReportResponses = reportService.getArticleReports(member);

        assertThat(articleReportResponses).hasSize(1);
        assertThat(articleReportResponses.get(0).getContent()).isEqualTo(ARTICLE_REPORT_CONTENT);
        assertThat(articleReportResponses.get(0).getIsFinished()).isEqualTo(false);
    }

    @DisplayName("ID로 개별 게시물 신고를 요청하면 해당 신고가 반환된다")
    @Test
    void readArticleReportTest() {
        when(articleReportRepository.findById(any())).thenReturn(Optional.of(articleReport));

        ArticleReportResponse articleReportResponse = reportService.readArticleReport(member, ARTICLE_REPORT_ID);

        assertThat(articleReportResponse).isNotNull();
        assertThat(articleReportResponse.getContent()).isEqualTo(ARTICLE_REPORT_CONTENT);
        assertThat(articleReportResponse.getIsFinished()).isEqualTo(false);
    }

    @DisplayName("예외 테스트 : ID로 잘못된 게시물 신고를 요청하면 ReportNotFoundException이 발생한다")
    @Test
    void readArticleReportExceptionTest() {
        when(articleReportRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reportService.readArticleReport(member, INVALID_REPORT_ID)).
                isInstanceOf(ReportNotFoundException.class).
                hasMessage(INVALID_REPORT_ID + "에 해당하는 신고 이력을 찾을 수 없습니다.");
    }

    @DisplayName("ID로 게시물 신고에 대한 삭제를 요청하면, 해당 신고를 삭제한다")
    @Test
    void deleteArticleReportTest() {
        when(articleReportRepository.findById(any())).thenReturn(Optional.of(articleReport));
        reportService.deleteArticleReport(member, ARTICLE_REPORT_ID);

        assertThat(articleReport.getIsFinished()).isTrue();
    }

    @DisplayName("예외 테스트 : ID로 잘못된 게시물 신고에 대한 삭제를 요청하면 ReportNotFoundException이 발생한다")
    @Test
    void deleteArticleReportExceptionTest() {
        doThrow(new ReportNotFoundException(INVALID_REPORT_ID)).
                when(articleReportRepository).findById(INVALID_REPORT_ID);

        assertThatThrownBy(() -> reportService.readArticleReport(member, INVALID_REPORT_ID)).
                isInstanceOf(ReportNotFoundException.class).
                hasMessage(INVALID_REPORT_ID + "에 해당하는 신고 이력을 찾을 수 없습니다.");
    }
}
