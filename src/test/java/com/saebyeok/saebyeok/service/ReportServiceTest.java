package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import com.saebyeok.saebyeok.domain.report.*;
import com.saebyeok.saebyeok.dto.report.ArticleReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.CommentReportCreateRequest;
import com.saebyeok.saebyeok.dto.report.ReportCategoryResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.ReportCategoryNotFoundException;
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
    public static final long COMMENT_REPORT_ID = 1L;
    public static final String COMMENT_REPORT_CONTENT = "이 댓글을 신고합니다";

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
    Article article;
    Comment comment;
    private CommentReport commentReport;

    @BeforeEach
    void setUp() {
        member = new Member(MEMBER_ID, MEMBER_OAUTH_ID, MEMBER_LOGIN_METHOD, LocalDateTime.now(), IS_DELETED, Role.USER, new ArrayList<>());
        article = new Article(ARTICLE_ID, "게시물", member, LocalDateTime.now(), false, false, null, new ArrayList<>());
        comment = new Comment();

        reportService = new ReportService(articleService, commentService, reportCategoryRepository, articleReportRepository, commentReportRepository);
        reportCategory = new ReportCategory(CATEGORY_ID, CATEGORY_NAME, CATEGORY_CONTENT);

        articleReport = new ArticleReport(ARTICLE_REPORT_ID, ARTICLE_REPORT_CONTENT, member, article, reportCategory, LocalDateTime.now(), false);
        commentReport = new CommentReport(COMMENT_REPORT_ID, COMMENT_REPORT_CONTENT, member, comment, reportCategory, LocalDateTime.now(), false);
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
    void createArticleReportTest() {
        ArticleReportCreateRequest createRequest = new ArticleReportCreateRequest(ARTICLE_REPORT_CONTENT, 1L, 1L);
        when(articleReportRepository.save(any(ArticleReport.class))).thenReturn(articleReport);
        when(reportCategoryRepository.findById(1L)).thenReturn(Optional.of(reportCategory));
        when(articleService.findArticleById(1L)).thenReturn(article);

        ArticleReport createdArticleReport = reportService.createArticleReport(member, createRequest);

        assertThat(createdArticleReport).isNotNull();
    }

    @DisplayName("예외 테스트 : 잘못된 게시물 ID로 신고를 생성하면 예외가 발생한다.")
    @Test
    void createArticleReportWithArticleExceptionTest() {
        Long invalidArticleId = 10000L;
        ArticleReportCreateRequest invalidRequest = new ArticleReportCreateRequest(ARTICLE_REPORT_CONTENT, invalidArticleId, 1L);
        when(reportCategoryRepository.findById(1L)).thenReturn(Optional.of(reportCategory));
        when(articleService.findArticleById(invalidArticleId)).thenThrow(new ArticleNotFoundException(invalidArticleId));

        assertThatThrownBy(() -> reportService.createArticleReport(member, invalidRequest)).
                isInstanceOf(ArticleNotFoundException.class).
                hasMessage(invalidArticleId + "에 해당하는 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : 잘못된 카테고리 ID로 게시글 신고를 생성하면 예외가 발생한다.")
    @Test
    void createArticleReportWithCategoryExceptionTest() {
        Long invalidCategoryId = 10000L;
        ArticleReportCreateRequest invalidRequest = new ArticleReportCreateRequest(ARTICLE_REPORT_CONTENT, 1L, invalidCategoryId);
        when(reportCategoryRepository.findById(invalidCategoryId)).thenThrow(new ReportCategoryNotFoundException(invalidCategoryId));

        assertThatThrownBy(() -> reportService.createArticleReport(member, invalidRequest)).
                isInstanceOf(ReportCategoryNotFoundException.class).
                hasMessage(invalidCategoryId + "에 해당하는 신고 분류를 찾을 수 없습니다.");
    }


    @DisplayName("댓글 신고 생성을 요청하면 댓글 신고가 생성된다")
    @Test
    void createCommentReportTest() {
        CommentReportCreateRequest createRequest = new CommentReportCreateRequest(COMMENT_REPORT_CONTENT, 1L, 1L);
        when(commentReportRepository.save(any(CommentReport.class))).thenReturn(commentReport);
        when(reportCategoryRepository.findById(1L)).thenReturn(Optional.of(reportCategory));
        when(commentService.findCommentById(1L)).thenReturn(comment);

        CommentReport createdCommentReport = reportService.createCommentReport(member, createRequest);

        assertThat(createdCommentReport).isNotNull();
    }

    @DisplayName("예외 테스트 : 잘못된 댓글 ID로 신고를 생성하면 예외가 발생한다.")
    @Test
    void createCommentReportWithCommentExceptionTest() {
        Long invalidCommentId = 10000L;
        CommentReportCreateRequest invalidRequest = new CommentReportCreateRequest(ARTICLE_REPORT_CONTENT, invalidCommentId, 1L);
        when(reportCategoryRepository.findById(1L)).thenReturn(Optional.of(reportCategory));
        when(commentService.findCommentById(invalidCommentId)).thenThrow(new CommentNotFoundException(invalidCommentId));

        assertThatThrownBy(() -> reportService.createCommentReport(member, invalidRequest)).
                isInstanceOf(CommentNotFoundException.class).
                hasMessage(invalidCommentId + "에 해당하는 댓글을 찾을 수 없습니다.");
    }

    @DisplayName("예외 테스트 : 잘못된 카테고리 ID로 댓글 신고를 생성하면 예외가 발생한다.")
    @Test
    void createCommentReportWithCategoryExceptionTest() {
        Long invalidCategoryId = 10000L;
        CommentReportCreateRequest invalidRequest = new CommentReportCreateRequest(ARTICLE_REPORT_CONTENT, 1L, invalidCategoryId);
        when(reportCategoryRepository.findById(invalidCategoryId)).thenThrow(new ReportCategoryNotFoundException(invalidCategoryId));

        assertThatThrownBy(() -> reportService.createCommentReport(member, invalidRequest)).
                isInstanceOf(ReportCategoryNotFoundException.class).
                hasMessage(invalidCategoryId + "에 해당하는 신고 분류를 찾을 수 없습니다.");
    }
}
