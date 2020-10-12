package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.saebyeok.saebyeok.domain.Comment.MAX_LENGTH;
import static com.saebyeok.saebyeok.domain.Comment.MIN_LENGTH;
import static com.saebyeok.saebyeok.domain.CommentTest.OVER_LENGTH_CONTENT;
import static com.saebyeok.saebyeok.domain.CommentTest.UNDER_LENGTH_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

class CommentAcceptanceTest extends AcceptanceTest {
    private static final Long NOT_EXIST_COMMENT_ID = 10L;
    private static final String RECOMMENT_CONTENT = "대댓글 내용입니다.";

    private Map<String, Object> params = new HashMap<>();

    @BeforeEach
    public void setUp() {
        super.setUp();

        createArticle(ARTICLE_CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);
    }

    /**
     *  댓글 A
     *  댓글 B
     *    └> 댓글 D
     *    └> 댓글 E
     *  댓글 C
     **/

    /**
     * Scenario: 댓글을 저장, 조회, 삭제할 수 있다.
     * <p>
     * given 로그인한 회원이 글을 저장했다.
     * when 로그인한 회원이 저장된 글에 댓글(A)을 쓰려고 한다.
     * then 댓글이 등록에 성공한다. (현재 댓글 1개, 대댓글 0개)
     * <p>
     * when 정해진 댓글의 최소 길이보다 짧은 댓글을 등록하려고 한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * when 정해진 댓글의 최대 길이보다 긴 댓글을 등록하려고 한다.
     * then 댓글 등록에 실패한다.
     * <p>
     * given 댓글을 2개(B, C) 등록한다.
     * when 게시글에 달린 댓글을 모두 조회한다.
     * then 댓글 목록의 조회에 성공한다. (A, B, C 순으로 조회된다.)
     * <p>
     * when 대댓글을 달고자 하는 댓글(B)을 선택하여 2개(D, E) 게시한다.
     * then 대댓글이 저장된다. (현재 댓글 3개, 대댓글 2개)
     * <p>
     * when 댓글 전체를 조회한다.
     * then 댓글 목록의 조회에 성공한다. (A -> B -> D -> E -> C 순으로 조회된다.)
     * <p>
     * when 존재하지 않는 댓글을 삭제한다.
     * then 댓글 삭제에 실패한다.
     * <p>
     * when 댓글을 삭제한다.
     * then 댓글 삭제에 성공한다.
     **/

    @DisplayName("댓글에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageComment() {
        //when 로그인한 회원이 저장된 글에 댓글(A)을 쓰려고 한다.
        Long commentAId = createCommentOf(ARTICLE_ID);

        //then 댓글이 등록에 성공한다. (현재 댓글 1개, 대댓글 0개)
        ArticleResponse articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments()).
                hasSize(1).
                extracting("id").
                contains(commentAId);

        //when 정해진 댓글의 최소 길이보다 짧은 댓글을 등록하려고 한다.
        ExceptionResponse exceptionResponse = createInvalidComment(UNDER_LENGTH_CONTENT);

        //then 댓글 등록에 실패한다.
        assertThat(exceptionResponse.getErrorMessage())
                .isEqualTo(String.format("댓글은 %d자 이상 %d자 이하로 작성할 수 있어요.", MIN_LENGTH, MAX_LENGTH));

        //when 정해진 댓글의 최대 길이보다 긴 댓글을 등록하려고 한다.
        ExceptionResponse overLengthExceptionResponse = createInvalidComment(OVER_LENGTH_CONTENT);

        //then 댓글 등록에 실패한다.
        assertThat(overLengthExceptionResponse.getErrorMessage())
                .isEqualTo(String.format("댓글은 %d자 이상 %d자 이하로 작성할 수 있어요.", MIN_LENGTH, MAX_LENGTH));

        //given 댓글을 2개(B, C) 등록한다.
        Long commentBId = createCommentOf(ARTICLE_ID);
        Long commentCId = createCommentOf(ARTICLE_ID);

        //when 게시글에 달린 댓글을 모두 조회한다.
        articleResponse = readArticle(ARTICLE_ID);

        //then 댓글 목록의 조회에 성공한다. (A, B, C 순으로 조회된다.)
        assertThat(articleResponse.getComments()).
                hasSize(3).
                extracting("id").
                containsExactly(commentAId, commentBId, commentCId);

        //when 대댓글을 달고자 하는 댓글(B)을 선택하여 2개(D, E) 게시한다.
        Long commentDId = createRecommentOf(ARTICLE_ID, commentBId);
        Long commentEId = createRecommentOf(ARTICLE_ID, commentBId);

        //then 대댓글이 저장된다. (현재 댓글 3개, 대댓글 2개)
        List<CommentResponse> commentResponses = readRecomments();
        assertThat(commentResponses).hasSize(2);

        //when 댓글 전체를 조회한다.
        articleResponse = readArticle(ARTICLE_ID);

        //then 댓글 목록의 조회에 성공한다. (A -> B -> D -> E -> C 순으로 조회된다.)
        assertThat(articleResponse.getComments()).
                hasSize(5).
                extracting("id").
                containsExactly(commentAId, commentBId, commentDId, commentEId, commentCId);

        //when 존재하지 않는 댓글을 삭제한다.
        ExceptionResponse commentNotFoundExceptionResponse = deleteNotFoundComment();

        //then 댓글 삭제에 실패한다.
        assertThat(commentNotFoundExceptionResponse.getErrorMessage())
                .contains("에 해당하는 댓글을 찾을 수 없습니다");

        //when 댓글을 삭제한다.
        deleteComment(1L);

        //then 댓글 삭제에 성공한다.
        articleResponse = readArticle(ARTICLE_ID);
        assertThat(articleResponse.getComments().stream().
                filter(commentResponse -> !commentResponse.getIsDeleted())).
                hasSize(4).
                extracting("id").
                doesNotContain(1L);
    }

    private ExceptionResponse createInvalidComment(String content) {
        //@formatter:off
        params.put("content", content);
        params.put("articleId", ARTICLE_ID);

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

    private Long createRecommentOf(Long articleId, Long commentId) {
        Map<String, Object> params = new HashMap<>();
        params.put("content", RECOMMENT_CONTENT);
        params.put("articleId", articleId);
        params.put("parentId", commentId);

        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post(API + "/articles/" + articleId + "/comments").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(Long.class);
        //@formatter:on
    }

    private List<CommentResponse> readRecomments() {
        //@formatter:off
        ArticleResponse articleResponse =
                given().
                        auth().oauth2(TOKEN).
                when().
                        get(API + "/articles/" + ARTICLE_ID).
                then().
                        log().all().
                        extract().
                        as(ArticleResponse.class);
        //@formatter:on

        return articleResponse.getComments().stream()
                .filter(comment -> !comment.getHasNoParent())
                .collect(Collectors.toList());
    }
}
