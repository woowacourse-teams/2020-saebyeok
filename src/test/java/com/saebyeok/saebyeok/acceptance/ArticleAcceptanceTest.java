package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleAcceptanceTest {
    public static final String CONTENT = "내용입니다";
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
        //given: 글이 하나도 없다.
        List<ArticleResponse> articles = getArticles();
        assertThat(articles).isEmpty();

        //when: 글을 하나 추가한다.
        createArticle(CONTENT, "기뻐요", true);

        //then: 글이 하나 있다.
        articles = getArticles();
        assertThat(articles).hasSize(1);

        //when: 글을 조회한다.
        ArticleResponse articleResponse = readArticle(1L);

        //then: 글의 내용이 방금 쓴 것과 일치한다.
        assertThat(articleResponse.getContent()).isEqualTo(CONTENT);

        //when: 글을 삭제한다.
        deleteArticle(1L);
        articles = getArticles();
        assertThat(articles).isEmpty();
    }


    private List<ArticleResponse> getArticles() {
        //@formatter:off
        return
                given().
                when().
                        get("/articles").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", ArticleResponse.class);
        //@formatter:on
    }

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
                post("/articles").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private ArticleResponse readArticle(long id) {
        //@formatter:off
        return
                given().
                when().
                        get("/articles/" + id).
                then().
                        log().all().
                        extract().
                        as(ArticleResponse.class);
        //@formatter:on
    }

    private void deleteArticle(long id) {
        //@formatter:off
        given().
                when().
                delete("/articles/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
