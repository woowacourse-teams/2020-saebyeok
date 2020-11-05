package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class LikeAcceptanceTest extends AcceptanceTest {
    private static final long COMMENT_ID = 1L;

    /**
     * Feature: 공감(좋아요) 기능
     * Scenario: 로그인 한 회원은 게시글과 댓글에 공감(좋아요)을 표현할 수 있다.
     * <p>
     * given 공감이 없는 게시글과 댓글이 등록되어있다.
     * when 회원이 게시글을 공감한다.
     * then 해당 게시글에 공감이 추가된다.
     * <p>
     * when 이미 공감한 글을 공감 취소한다.
     * then 해당 게시글에 적용된 공감이 제거된다.
     * <p>
     * when 회원이 댓글을 공감한다.
     * then 해당 댓글에 공감이 추가된다.
     * <p>
     * when 이미 공감한 댓글을 공감 취소한다.
     * then 해당 댓글에 적용된 공감이 제거된다.
     **/
    @DisplayName("게시글과 댓글에 공감(좋아요)을 표현할 수 있다.")
    @Test
    void manageLike() {
        // given 공감이 없는 게시글과 댓글이 등록되어있다.
        createArticle(ARTICLE_CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);
        createCommentOf(ARTICLE_ID);

        // when 회원이 게시글을 공감한다.
        likeArticle(ARTICLE_ID);

        // then 해당 게시글에 공감이 추가된다.
        ArticleResponse likedArticle = readArticle(ARTICLE_ID);
        assertThat(likedArticle.getLikesCount()).isEqualTo(1);

        // when 이미 공감한 글을 공감 취소한다.
        unlikeArticle(ARTICLE_ID);

        // then 해당 게시글에 적용된 공감이 제거된다.
        ArticleResponse unLikedArticle = readArticle(ARTICLE_ID);
        assertThat(unLikedArticle.getLikesCount()).isZero();

        // when 회원이 댓글을 공감한다.
        likeComment(ARTICLE_ID, COMMENT_ID);

        // then 해당 댓글에 공감이 추가된다.
        CommentResponse likedComment = getComments(ARTICLE_ID).get(0);
        assertThat(likedComment.getLikesCount()).isEqualTo(1);

        // when 이미 공감한 댓글을 공감 취소한다.
        unlikeComment(ARTICLE_ID, COMMENT_ID);

        // then 해당 댓글에 적용된 공감이 제거된다.
        CommentResponse unLikedComment = getComments(ARTICLE_ID).get(0);
        assertThat(unLikedComment.getLikesCount()).isZero();
    }

    private void likeArticle(Long articleId) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                post(API + "/articles/" + articleId + "/likes").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private void unlikeArticle(Long articleId) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/articles/" + articleId + "/likes").
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }

    private void likeComment(Long articleId, Long commentId) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                post(API + "/articles/" + articleId + "/comments/" + commentId + "/likes").
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value());
        //@formatter:on
    }

    private void unlikeComment(Long articleId, Long commentId) {
        //@formatter:off
        given().
                auth().oauth2(TOKEN).
        when().
                delete(API + "/articles/" + articleId + "/comments/" + commentId + "/likes").
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        //@formatter:on
    }
}
