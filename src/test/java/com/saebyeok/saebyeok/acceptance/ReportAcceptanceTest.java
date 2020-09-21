package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.report.ArticleReportResponse;
import com.saebyeok.saebyeok.dto.report.CommentReportResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportAcceptanceTest extends AcceptanceTest {
    private static final Long EMOTION_ID = 1L;
    private static final List<Long> SUB_EMOTION_IDS = Arrays.asList(1L, 2L);
    private static final Long REPORT_ID = 1L;
    private static final Long REPORT_CATEGORY_ID = 1L;
    private static final Long REPORT_ARTICLE_ID = 1L;
    private static final Long REPORT_COMMENT_ID = 1L;
    private static final String REPORT_CONTENT = "이 게시물을 신고합니다.";

    @BeforeEach
    public void setUp() {
        super.setUp();

        createArticle("content", EMOTION_ID, SUB_EMOTION_IDS, true);
        createComment(REPORT_COMMENT_ID);
    }


    /**
     * Scenario: 신고를 추가, 조회, 삭제할 수 있다.
     * <p>
     * given 게시물 신고가 하나도 없다.
     * when 게시물 신고를 하나 추가한다.
     * then 게시물 신고가 1개 생긴다.
     * <p>
     * given 댓글 신고가 하나도 없다.
     * when 댓글 신고를 하나 추가한다.
     * then 댓글 신고가 1개 생긴다.
     * <p>
     * given 게시물 신고가 1개 존재한다.
     * when 게시물 신고를 조회한다.
     * then 조회된 게시물 신고 값이 존재하는 신고 값과 일치한다.
     * <p>
     * given 댓글 신고가 1개 존재한다.
     * when 댓글 신고를 조회한다.
     * then 조회된 댓글 신고 값이 존재하는 신고 값과 일치한다.
     * <p>
     * given 게시물 신고가 하나 있다.
     * when 해당 게시물 신고를 완료 처리한다.
     * then 해당 게시물 신고를 조회하면 완료된 것으로 값이 설정되어 있다.
     * <p>
     * given 댓글 신고가 하나 있다.
     * when 해당 댓글 신고를 완료 처리한다.
     * then 해당 댓글 신고를 조회하면 완료된 것으로 값이 설정되어 있다.
     */

    @Test
    void manageArticleReport() {
        //given 신고가 하나도 없다.
        List<ArticleReportResponse> reports = getArticleReports();
        assertThat(reports).isEmpty();

        //when 신고를 하나 추가한다.
        createArticleReport();

        //then 신고가 1개 생긴다.
        reports = getArticleReports();
        assertThat(reports).hasSize(1);

        //when 신고를 조회한다.
        ArticleReportResponse reportResponse = readArticleReport(REPORT_ID);

        //then 조회된 신고 값이 존재하는 신고 값과 일치한다.
        assertThat(reportResponse.getContent()).isEqualTo(REPORT_CONTENT);

        //when 신고를 완료 처리한다.
        deleteArticleReport(REPORT_ID);

        //then 해당 신고를 조회하면 완료된 것으로 값이 설정되어 있다.
        reportResponse = readArticleReport(REPORT_ID);
        assertThat(reportResponse.getIsFinished()).isTrue();
    }

    @Test
    void manageCommentReport() {
        //given 신고가 하나도 없다.
        List<CommentReportResponse> reports = getCommentReports();
        assertThat(reports).isEmpty();

        //when 신고를 하나 추가한다.
        createCommentReport();

        //then 신고가 1개 생긴다.
        reports = getCommentReports();
        assertThat(reports).hasSize(1);

        //when 신고를 조회한다.
        CommentReportResponse reportResponse = readCommentReport(REPORT_ID);

        //then 조회된 신고 값이 존재하는 신고 값과 일치한다.
        assertThat(reportResponse.getContent()).isEqualTo(REPORT_CONTENT);

        //when 신고를 완료 처리한다.
        deleteCommentReport(REPORT_ID);

        //then 해당 신고를 조회하면 완료된 것으로 값이 설정되어 있다.
        reportResponse = readCommentReport(REPORT_ID);
        assertThat(reportResponse.getIsFinished()).isTrue();
    }

    private void createArticleReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("content", REPORT_CONTENT);
        params.put("articleId", REPORT_ARTICLE_ID);
        params.put("reportCategoryId", REPORT_CATEGORY_ID);

        //@formatter:off
        given().
                auth().oauth2(TOKEN).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post(API + "/reports/article").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private List<ArticleReportResponse> getArticleReports() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/reports/article").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", ArticleReportResponse.class);
        //@formatter:on
    }

    private ArticleReportResponse readArticleReport(Long id) {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get(API + "/reports/article/" + id).
                then().
                        log().all().
                        extract().
                        as(ArticleReportResponse.class);
        //@formatter:on
    }

    private void deleteArticleReport(Long id) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/reports/article/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }

    private void createCommentReport() {
        Map<String, Object> params = new HashMap<>();
        params.put("content", REPORT_CONTENT);
        params.put("commentId", REPORT_COMMENT_ID);
        params.put("reportCategoryId", REPORT_CATEGORY_ID);

        //@formatter:off
        given().
                auth().oauth2(TOKEN).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post(API + "/reports/comment").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private List<CommentReportResponse> getCommentReports() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/reports/comment").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", CommentReportResponse.class);
        //@formatter:on
    }

    private CommentReportResponse readCommentReport(Long id) {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get(API + "/reports/comment/" + id).
                then().
                        log().all().
                        extract().
                        as(CommentReportResponse.class);
        //@formatter:on
    }

    private void deleteCommentReport(Long id) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/reports/comment/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
