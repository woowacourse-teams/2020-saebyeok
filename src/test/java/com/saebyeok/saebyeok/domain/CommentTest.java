package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {
    private static final String TEST_CONTENT = "새벽 좋아요";
    private static final String TEST_NICKNAME = "시라소니";

    @DisplayName("Comment를 생성할 때 값들이 정상적으로 생성되야 한다")
    @Test
    void createCommentTest() {
        Member member = new Member();
        Article article = new Article();

        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();

        assertThat(comment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(comment.getArticle()).isEqualTo(article);
        assertThat(comment.getIsDeleted()).isFalse();
    }

}
