package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
class CommentAcceptanceTest {
    private static final String UNDER_LENGTH_EXCEPTION_MESSAGE = "댓글의 최소 길이는 1글자입니다!";
    private static final String OVER_LENGTH_EXCEPTION_MESSAGE = "댓글의 최대 길이는 140자입니다!";
    private static final String COMMENT_NOT_FOUND_EXCEPTION_MESSAGE = "해당 댓글을 찾을 수 없습니다!";
    private static final long COMMENT_ID = 1L;
    private static final long NOT_EXIST_COMMENT_ID = 10L;

    @LocalServerPort
    int port;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article;
    private Member member;
    private Map<String, Object> params;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        article = new Article();
        member = new Member();
        params = new HashMap<>();
        params.put("member", member);
        params.put("nickname", "시라소니");
        params.put("createdDate", LocalDateTime.now());
        params.put("article", article);
        params.put("isDeleted", false);

        articleRepository.save(article);
        memberRepository.save(member);
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
        createComment();
        //then
        // TODO: 2020/07/15 댓글과 연관관계가 있는 Article을 통해 댓글 정보를 조회하고, 저장이 되었는지 확인한다.

        //given
        String underLengthContent = " ";
        //when
        ExceptionResponse underLengthExceptionResponse = createInvalidComment(underLengthContent);
        //then
        assertThat(underLengthExceptionResponse.getErrorMessage()).isEqualTo(UNDER_LENGTH_EXCEPTION_MESSAGE);

        //given
        String overLengthContent = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
            "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
            "1만4000원.\n";
        //when
        ExceptionResponse overLengthExceptionResponse = createInvalidComment(overLengthContent);
        //then
        assertThat(overLengthExceptionResponse.getErrorMessage()).isEqualTo(OVER_LENGTH_EXCEPTION_MESSAGE);

        // TODO: 2020/07/15 댓글 조회의 경우, 댓글과 연관관계에 있는 Article을 불러와 getComments를 해서 확인해야 한다.

        //when
        deleteComment();
        //then
        // TODO: 2020/07/15 댓글 삭제가 수행되었는지 댓글과 연관관계에 있는 Article을 통해 댓글을 조회하여 확인해야 한다.

        //when
        ExceptionResponse commentNotFoundExceptionResponse = deleteNotFoundComment();
        //then
        assertThat(commentNotFoundExceptionResponse.getErrorMessage()).isEqualTo(COMMENT_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    private void createComment() {
        //@formatter:off
        params.put("content", "새벽 좋아요");

        given().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
        when().
                post("/articles/" + article.getId() + "/comments").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                header("Location","/articles/" + article.getId());
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
                    post("/articles/" + article.getId() + "/comments").
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }

    private void deleteComment() {
        //@formatter:off
        given().
        when().
                delete("/articles/" + article.getId() + "/comments/" + COMMENT_ID).
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
                    delete("/articles/" + article.getId() + "/comments/" + NOT_EXIST_COMMENT_ID).
            then().
                    log().all().
                    statusCode(HttpStatus.BAD_REQUEST.value()).
                    extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
