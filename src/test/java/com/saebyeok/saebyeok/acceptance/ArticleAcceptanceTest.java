package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleAcceptanceTest extends AcceptanceTest {
    private static final Integer PAGE_NUMBER = 0;
    private static final Integer PAGE_SIZE = 10;

    /**
     * Scenario: 게시글을 조회, 삭제할 수 있다
     * <p>
     * given 글이 하나도 없다.
     * when 글을 하나 추가한다.
     * then 글이 1개 있다.
     * <p>
     * given 아까 쓴 글이 있다.
     * when 글을 조회한다.
     * then 글의 내용이 방금 쓴 것과 일치한다.
     * <p>
     * given 글이 하나 있다.
     * when 글을 삭제한다.
     * then 이제 글이 하나도 없다.
     */

    @DisplayName("게시물에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageArticle() {
        //given 글이 하나도 없다.
        List<ArticleResponse> articles = getArticles();
        assertThat(articles).isEmpty();

        //when 글을 하나 추가한다.
        createArticle(ARTICLE_CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);

        //then 글이 하나 있다.
        articles = getArticles();
        assertThat(articles).hasSize(1);

        //when 글을 조회한다.
        ArticleResponse articleResponse = readArticle(ARTICLE_ID);

        //then 글의 내용이 방금 쓴 것과 일치한다.
        assertThat(articleResponse.getContent()).isEqualTo(ARTICLE_CONTENT);

        //when 글을 삭제한다.
        deleteArticle(ARTICLE_ID);
        articles = getArticles();
        assertThat(articles).isEmpty();
    }


    private List<ArticleResponse> getArticles() {
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        pathParam("page", PAGE_NUMBER).
                        pathParam("size", PAGE_SIZE).
                when().
                        get(API + "/articles?page={page}&size={size}").
                then().
                        log().all().
                        extract().
                        jsonPath().
                        getList(".", ArticleResponse.class);
        //@formatter:on
    }

    private void deleteArticle(Long id) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/articles/" + id).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
