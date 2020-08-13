package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * given: 글이 하나도 없다.
 * when: 글을 하나 추가한다.
 * then: 글이 1개 있다.
 * <p>
 * given: 아까 쓴 글이 있다.
 * when: 글을 조회한다.
 * then: 글의 내용이 방금 쓴 것과 일치한다.
 * <p>
 * given: 글이 하나 있다.
 * when: 글을 삭제한다.
 * then: 이제 글이 하나도 없다.
 */
@Disabled
@ActiveProfiles("test")
@Sql({"/truncate.sql", "/emotion.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleAcceptanceTest {
    private static final String API = "/api";
    private static final String CONTENT = "내용입니다";
    private static final Long EMOTION_ID = 1L;
    private static final List<Long> SUB_EMOTION_IDS = Arrays.asList(1L, 2L);
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final Long ARTICLE_ID = 1L;

    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void manageArticle() {
        // Todo: 초기 개발을 위해 Article과 연관관계가 있는 Member를 data.sql에서 수동 생성함.
        //  Member 관련 인수테스트가 생기면 삭제하고 아래에 코드 추가하기

        //given: 글이 하나도 없다.
        List<ArticleResponse> articles = getArticles();
        assertThat(articles).isEmpty();

        //when: 글을 하나 추가한다.
        createArticle(CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);

        //then: 글이 하나 있다.
        articles = getArticles();
        assertThat(articles).hasSize(1);

        //when: 글을 조회한다.
        ArticleResponse articleResponse = readArticle(ARTICLE_ID);

        //then: 글의 내용이 방금 쓴 것과 일치한다.
        assertThat(articleResponse.getContent()).isEqualTo(CONTENT);

        //when: 글을 삭제한다.
        deleteArticle(ARTICLE_ID);
        articles = getArticles();
        assertThat(articles).isEmpty();
    }


    private List<ArticleResponse> getArticles() {
        //@formatter:off
        return
                given().
                        pathParam("page", PAGE_NUMBER).
                        pathParam("size", PAGE_SIZE).
                when().
                        get(API + "/articles/?page={page}&size={size}").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", ArticleResponse.class);
        //@formatter:on
    }

    private void createArticle(String content, Long emotionId, List<Long> subEmotionIds, Boolean isCommentAllowed) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", content);
        params.put("emotionId", emotionId);
        params.put("subEmotionIds", subEmotionIds);
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

    private void deleteArticle(Long id) {
        //@formatter:off
        given().
        when().
                delete(API + "/articles/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
