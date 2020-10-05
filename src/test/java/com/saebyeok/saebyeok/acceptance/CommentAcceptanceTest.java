package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.saebyeok.saebyeok.domain.CommentTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class CommentAcceptanceTest extends AcceptanceTest {
    private static final Long EMOTION_ID = 1L;
    private static final List<Long> SUB_EMOTION_IDS = Arrays.asList(1L, 2L);
    private static final Long NOT_EXIST_COMMENT_ID = 10L;

    private Map<String, Object> params;

    @BeforeEach
    public void setUp() {
        super.setUp();

        createArticle("content", EMOTION_ID, SUB_EMOTION_IDS, true);

        params = new HashMap<>();
        params.put("memberId", MEMBER_ID);
        params.put("nickname", TEST_NICKNAME);
        params.put("articleId", ARTICLE_ID);
        params.put("isDeleted", false);
    }

    /**
     * Scenario: 댓글을 저장, 조회, 삭제할 수 있다.
     * <p>
     * given 로그인한 회원이 글을 저장했다.
     * when 로그인한 회원이 저장된 글에 댓글을 쓰려고 한다.
     * then 댓글이 등록에 성공한다.
     * <p>
     * when 정해진 댓글의 최소 길이보다 짧은 댓글을 등록하려고 한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * when 정해진 댓글의 최대 길이보다 긴 댓글을 등록하려고 한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * given 댓글을 여러 개 등록한다.
     * when 게시글에 달린 댓글을 모두 조회한다.
     * then 댓글 목록의 조회에 성공한다.
     * <p>
     * when 댓글을 삭제한다.
     * then 댓글 삭제에 성공한다.
     * <p>
     * when 존재하지 않는 댓글을 삭제한다.
     * then 댓글 삭제에 실패한다.
     **/

    @DisplayName("댓글에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageComment() {
        //when 로그인한 회원이 저장된 글에 댓글을 쓰려고 한다.
        createComment(1L);

        //then 댓글이 등록에 성공한다.
        ArticleResponse articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments()).
                hasSize(1).
                extracting("content").
                contains(TEST_CONTENT);

        //when 정해진 댓글의 최소 길이보다 짧은 댓글을 등록하려고 한다.
        ExceptionResponse exceptionResponse = createInvalidComment(UNDER_LENGTH_CONTENT);
        int length = UNDER_LENGTH_CONTENT.trim().length();

        //then 댓글 등록에 실패한다.
        assertThat(exceptionResponse.getErrorMessage())
                .isEqualTo(String.format("댓글은 %d자 이상 %d자 이하로 작성할 수 있어요.", Comment.MIN_LENGTH, Comment.MAX_LENGTH));

        //when 정해진 댓글의 최대 길이보다 긴 댓글을 등록하려고 한다.
        ExceptionResponse overLengthExceptionResponse = createInvalidComment(OVER_LENGTH_CONTENT);
        length = OVER_LENGTH_CONTENT.trim().length();

        //then 댓글 등록에 실패한다.
        assertThat(overLengthExceptionResponse.getErrorMessage())
                .isEqualTo(String.format("댓글은 %d자 이상 %d자 이하로 작성할 수 있어요.", Comment.MIN_LENGTH, Comment.MAX_LENGTH));

        //given 댓글을 여러 개 등록한다.
        createComment(2L);
        createComment(3L);

        //when 게시글에 달린 댓글을 모두 조회한다.
        articleResponse = readArticle(ARTICLE_ID);

        //then 댓글 목록의 조회에 성공한다.
        assertThat(articleResponse.getComments()).
                hasSize(3).
                extracting("id").
                containsOnly(1L, 2L, 3L);

        //when 댓글을 삭제한다.
        deleteComment(1L);

        //then 댓글 삭제에 성공한다.
        articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments().stream().
                filter(commentResponse -> !commentResponse.getIsDeleted())).
                hasSize(2).
                extracting("id").
                doesNotContain(1L);

        //when 존재하지 않는 댓글을 삭제한다.
        ExceptionResponse commentNotFoundExceptionResponse = deleteNotFoundComment();

        //then 댓글 삭제에 실패한다.
        assertThat(commentNotFoundExceptionResponse.getErrorMessage())
                .contains("에 해당하는 댓글을 찾을 수 없습니다!");
    }

    private void createComment(Long createdId) {
        //@formatter:off
        params.put("content", "새벽 좋아요");

        given().
                auth().oauth2(TOKEN).
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post(API + "/articles/" + ARTICLE_ID + "/comments").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                header("Location","/articles/" + ARTICLE_ID + "/comments/" + createdId);
        //@formatter:on
    }

    private ExceptionResponse createInvalidComment(String content) {
        //@formatter:off
        params.put("content", content);

        return
            given().
                    auth().oauth2(TOKEN).
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                    post(API + "/articles/" + ARTICLE_ID + "/comments").
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }

    private void deleteComment(Long deletedId) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/articles/" + ARTICLE_ID + "/comments/" + deletedId).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }

    private ExceptionResponse deleteNotFoundComment() {
        //@formatter:off
        return
            given().
                    auth().oauth2(TOKEN).
            when().
                    delete(API + "/articles/" + ARTICLE_ID + "/comments/" + NOT_EXIST_COMMENT_ID).
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
