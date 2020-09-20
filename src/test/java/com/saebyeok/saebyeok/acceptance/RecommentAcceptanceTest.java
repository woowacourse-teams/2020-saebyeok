package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.dto.RecommentResponse;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RecommentAcceptanceTest extends AcceptanceTest {
    private static final Long COMMENT_ID = 1L;
    private static final String RECOMMENT_CONTENT = "대댓글 내용입니다.";

    @BeforeEach
    public void setUp() {
        super.setUp();
        createArticle(ARTICLE_CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);
        createCommentOf(ARTICLE_ID);
        createCommentOf(ARTICLE_ID);
    }

    /**
     * Scenario: 대댓글을 저장, 삭제할 수 있다.
     * <p>
     * given DB에 게시물과 댓글들이 저장되어 있다.
     * when 대댓글을 달고자 하는 댓글을 선택하여 게시한다.
     * then 선택한 댓글 아래에 대댓글이 저장된다.
     * <p>
     * when 존재하지 않는 대댓글을 삭제한다.
     * then 대댓글 삭제에 실패한다.
     * <p>
     * when 존재하는 대댓글을 삭제한다.
     * then 대댓글 삭제에 성공한다.
     **/

    @DisplayName("대댓글에 대해 요청을 보낼 때, 응답이 올바르게 수행되어야 한다")
    @Test
    void manageRecomment() {
        //when 대댓글을 달고자 하는 댓글을 선택하여 게시한다.
        Long savedRecommentId = createRecommentOf(COMMENT_ID);

        //then 선택한 댓글 아래에 대댓글이 저장된다.
        List<RecommentResponse> recommentResponses = readRecommentOf(COMMENT_ID);
        assertThat(recommentResponses).hasSize(1);

        //when 존재하지 않는 대댓글을 삭제한다.
        ExceptionResponse recommentNotFoundExceptionResponse = deleteNotFoundRecomment();

        //then 대댓글 삭제에 실패한다.
        assertThat(recommentNotFoundExceptionResponse.getErrorMessage())
                .contains("에 해당하는 대댓글을 찾을 수 없습니다!");

        //when 존재하는 대댓글을 삭제한다.
        deleteRecomment(savedRecommentId);

        //then 대댓글 삭제에 성공한다.
        recommentResponses = readRecommentOf(COMMENT_ID);
        assertThat(recommentResponses.stream().
                filter(recommentResponse -> !recommentResponse.getIsDeleted())).
                hasSize(0);
    }

    private Long createRecommentOf(Long commentId) {
        Map<String, String> params = new HashMap<>();
        params.put("content", RECOMMENT_CONTENT);
        //@formatter:off
        return
                given().
                        auth().oauth2(TOKEN).
                        body(params).
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                        post(API + "/articles/" + ARTICLE_ID + "/comments/" + commentId + "/recomments").
                then().
                        log().all().
                        statusCode(HttpStatus.CREATED.value()).
                        extract().as(Long.class);
        //@formatter:on
    }

    private List<RecommentResponse> readRecommentOf(Long commentId) {
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

        CommentResponse commentResponse = articleResponse.getComments().stream()
                .filter(response -> response.getId().equals(commentId))
                .findFirst().orElseThrow(() -> new CommentNotFoundException(commentId));

        return commentResponse.getRecomments();
    }

    private ExceptionResponse deleteNotFoundRecomment() {
        return null;
    }

    private void deleteRecomment(Long recommentId) {
        return;
    }
}
