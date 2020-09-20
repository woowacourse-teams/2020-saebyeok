package com.saebyeok.saebyeok.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RecommentAcceptanceTest extends AcceptanceTest {
    private static final Long COMMENT_ID = 1L;

    @BeforeEach
    public void setUp() {
        createArticle(ARTICLE_CONTENT, EMOTION_ID, SUB_EMOTION_IDS, true);
        createCommentOf(ARTICLE_ID);
        createCommentOf(ARTICLE_ID);
    }

    /**
     * Scenario: 대댓글을 저장, 조회, 삭제할 수 있다.
     * <p>
     * given DB에 게시물과 댓글들이 저장되어 있다.
     * when 대댓글을 달고자 하는 댓글을 선택하여 게시한다.
     * then 선택한 댓글 아래에 대댓글이 저장된다.
     * <p>
     * when 특정 댓글의 대댓글을 모두 조회한다.
     * then 해당 대댓글 목록 조회에 성공한다.
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
        //then 선택한 댓글 아래에 대댓글이 저장된다.

        //when 특정 댓글의 대댓글을 모두 조회한다.
        //then 해당 대댓글 목록 조회에 성공한다.

        //when 존재하지 않는 대댓글을 삭제한다.
        //then 대댓글 삭제에 실패한다.

        //when 존재하는 대댓글을 삭제한다.
        //then 대댓글 삭제에 성공한다.
    }
}
