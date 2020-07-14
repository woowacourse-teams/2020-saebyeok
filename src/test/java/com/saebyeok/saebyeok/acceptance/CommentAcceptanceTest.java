package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentAcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Scenario: 댓글을 저장, 조회, 삭제할 수 있다.
     * <p>
     * given 로그인한 회원이 글을 저장했다.
     * and 로그인한 회원이 저장된 글에 댓글을 쓰려고 한다.
     * when 댓글을 등록한다.
     * then 댓글이 등록에 성공한다.
     * <p>
     * when 정해진 댓글의 최소 길이보다 짧은 댓글을 등록한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * when 정해진 댓글의 최대 길이보다 긴 댓글을 등록한다.
     * then 댓글 등록에 실패한다.
     **/

    @DisplayName("댓글에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageComment() {
        //given
        Article article = new Article();
        Member member = new Member();
        //when
        Long id = createComment(article, member);
        //then
        assertThat(id).isEqualTo(1L);

        //when
        ExceptionResponse response1 = createUnderLengthComment(article, member);
        //then
        assertThat(response1.getErrorMessage()).isEqualTo("댓글의 최소 길이는 한 글자입니다!");

        //when
        ExceptionResponse response2 = createOverLengthComment(article, member);
        //then
        assertThat(response2.getErrorMessage()).isEqualTo("댓글의 최대 길이는 140자입니다!");
    }

    private Long createComment(Article article, Member member) {
        //@formatter:off
        Map<String, Object> params = new HashMap<>();
        params.put("content", "새벽 좋아요");
        params.put("member", member);
        params.put("nickname", "시라소니");
        params.put("createdDate", LocalDateTime.now());
        params.put("article", article);
        params.put("isDeleted", false);

        return
            given().
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                    post("/articles/" + 1L + "/comments").
            then().
                    log().all().
                    statusCode(HttpStatus.CREATED.value()).
                    extract().as(Long.class);
        //@formatter:on
    }

    private ExceptionResponse createUnderLengthComment(Article article, Member member) {
        //@formatter:off
        Map<String, Object> params = new HashMap<>();
        params.put("content", " ");
        params.put("member", member);
        params.put("nickname", "시라소니");
        params.put("createdDate", LocalDateTime.now());
        params.put("article", article);
        params.put("isDeleted", false);

        return
            given().
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                    post("/articles/" + 1L + "/comments").
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }

    private ExceptionResponse createOverLengthComment(Article article, Member member) {
        //@formatter:off
        Map<String, Object> params = new HashMap<>();
        params.put("content", "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
            "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
            "1만4000원.\n");
        params.put("member", member);
        params.put("nickname", "시라소니");
        params.put("createdDate", LocalDateTime.now());
        params.put("article", article);
        params.put("isDeleted", false);

        return
            given().
                    body(params).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    accept(MediaType.APPLICATION_JSON_VALUE).
            when().
                    post("/articles/" + 1L + "/comments").
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
