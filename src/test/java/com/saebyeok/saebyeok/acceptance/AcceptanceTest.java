package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WithUserDetails("123456789")
@Sql({"/truncate.sql", "/emotion.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    static final String API = "/api";
    static final Long ARTICLE_ID = 1L;
    static final String ARTICLE_CONTENT = "내용입니다";
    static final String COMMENT_CONTENT = "새벽 좋아요";
    static final Long EMOTION_ID = 1L;
    static final List<Long> SUB_EMOTION_IDS = Arrays.asList(1L, 2L);
    static String TOKEN = null;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    protected void setUp() {
        RestAssured.port = port;
        TOKEN = jwtTokenProvider.createToken("123456789");
    }

    protected ArticleResponse readArticle(Long id) {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get(API + "/articles/" + id).
                then().
                        log().all().
                        extract().
                        as(ArticleResponse.class);
        //@formatter:on
    }

    protected void createArticle(String content, Long emotionId, List<Long> subEmotionIds, Boolean isCommentAllowed) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("emotionId", emotionId);
        params.put("subEmotionIds", subEmotionIds);
        params.put("isCommentAllowed", isCommentAllowed.toString());

        //@formatter:off
        given().
                auth().oauth2(TOKEN).
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

    protected Long createCommentOf(Long targetArticleId) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", COMMENT_CONTENT);
        params.put("articleId", targetArticleId);

        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post(API + "/articles/" + targetArticleId + "/comments").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(Long.class);
        //@formatter:on
    }

    protected List<CommentResponse> getComments(Long articleId) {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        get(API + "/articles/" + articleId + "/comments").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", CommentResponse.class);
        //@formatter:on
    }
}