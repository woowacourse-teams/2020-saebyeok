package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static com.saebyeok.saebyeok.domain.CommentTest.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@Sql("/truncate.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentAcceptanceTest {
    private static final String API = "/api";
    private static final Long ARTICLE_ID = 1L;
    private static final Long MEMBER_ID = 1L;
    // TODO: 2020/07/20 MEMBER_ID는 data.sql에 있는 맴버를 가리킨다. 이후 맴버가 구현되면 고쳐야됨.
    private static final Long NOT_EXIST_COMMENT_ID = 10L;

    @LocalServerPort
    int port;

    private Map<String, Object> params;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        createArticle("content", "emotion", true);

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
     * and 로그인한 회원이 저장된 글에 댓글을 쓰려고 한다.
     * when 댓글을 등록한다.
     * then 댓글이 등록에 성공한다.
     * <p>
     * given 정해진 댓글의 최소 길이보다 짧은 댓글을 등록하려고 한다.
     * when 댓글을 등록한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * given 정해진 댓글의 최대 길이보다 긴 댓글을 등록하려고 한다.
     * when 댓글을 등록한다.
     * then 댓글 등록에 실패한다.
     * <p>
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
        //given
        //when
        createComment(1L);
        //then
        ArticleResponse articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments()).
                hasSize(1).
                extracting("content").
                contains(TEST_CONTENT);

        //given
        //when
        ExceptionResponse exceptionResponse = createInvalidComment(UNDER_LENGTH_CONTENT);
        int length = UNDER_LENGTH_CONTENT.trim().length();
        //then
        assertThat(exceptionResponse.getErrorMessage())
                .contains("글자수가 " + length + "이므로 올바르지 않은 댓글입니다!");

        //given
        //when
        ExceptionResponse overLengthExceptionResponse = createInvalidComment(OVER_LENGTH_CONTENT);
        length = OVER_LENGTH_CONTENT.trim().length();
        //then
        assertThat(overLengthExceptionResponse.getErrorMessage())
                .contains("글자수가 " + length + "이므로 올바르지 않은 댓글입니다!");

        //given
        createComment(2L);
        createComment(3L);
        //when
        articleResponse = readArticle(ARTICLE_ID);
        //then
        assertThat(articleResponse.getComments()).
                hasSize(3).
                extracting("id").
                containsOnly(1L, 2L, 3L);

        //given
        //when
        deleteComment(1L);
        //then
        articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments()).
                hasSize(2).
                extracting("id").
                doesNotContain(1L);

        //when
        ExceptionResponse commentNotFoundExceptionResponse = deleteNotFoundComment();
        //then
        assertThat(commentNotFoundExceptionResponse.getErrorMessage())
                .contains("에 해당하는 댓글을 찾을 수 없습니다!");
    }

    private void createComment(Long createdId) {
        //@formatter:off
        params.put("content", "새벽 좋아요");

        given().
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
            when().
                    delete(API + "/articles/" + ARTICLE_ID + "/comments/" + NOT_EXIST_COMMENT_ID).
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }

    // TODO: 2020/07/20 나중에 코드 통합시 중복 제거해야됨. Article 인수테스트 코드 그대로 사용..
    private void createArticle(String content, String emotion, Boolean isCommentAllowed) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("emotion", emotion);
        params.put("isCommentAllowed", isCommentAllowed.toString());

        //@formatter:off
        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post(API + "/articles").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private ArticleResponse readArticle(Long id) {
        //@formatter:off
        return
            given().
                    accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                    get(API + "/articles/" + id).
            then().
                    log().all().
                    extract().
                    as(ArticleResponse.class);
        //@formatter:on
    }
}
