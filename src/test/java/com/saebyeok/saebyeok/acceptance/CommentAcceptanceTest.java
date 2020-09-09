package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.saebyeok.saebyeok.domain.CommentTest.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@WithUserDetails("123456789")
@ActiveProfiles("test")
@Sql({"/truncate.sql", "/emotion.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommentAcceptanceTest {
    private static final String API = "/api";
    private static final Long ARTICLE_ID = 1L;
    private static final Long EMOTION_ID = 1L;
    private static final List<Long> SUB_EMOTION_IDS = Arrays.asList(1L, 2L);
    private static final Long MEMBER_ID = 1L;
    // TODO: 2020/07/20 MEMBER_ID는 data.sql에 있는 맴버를 가리킨다. 이후 맴버가 구현되면 고쳐야됨.
    private static final Long NOT_EXIST_COMMENT_ID = 10L;
    private static String token = null;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    int port;

    private Map<String, Object> params;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = jwtTokenProvider.createToken("123456789");

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
                auth().oauth2(token).
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
                    auth().oauth2(token).
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
                auth().oauth2(token).
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
                    auth().oauth2(token).
            when().
                    delete(API + "/articles/" + ARTICLE_ID + "/comments/" + NOT_EXIST_COMMENT_ID).
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }

    // TODO: 2020/07/20 나중에 코드 통합시 중복 제거해야됨. Article 인수테스트 코드 그대로 사용..
    private void createArticle(String content, Long emotionId, List<Long> subEmotionIds, Boolean isCommentAllowed) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("emotionId", emotionId);
        params.put("subEmotionIds", subEmotionIds);
        params.put("isCommentAllowed", isCommentAllowed.toString());

        //@formatter:off
        given().
                auth().oauth2(token).
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
                    auth().oauth2(token).
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
