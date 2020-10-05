package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
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

    // TODO: 2020/10/05 article, comment create 테스트 구현
}
