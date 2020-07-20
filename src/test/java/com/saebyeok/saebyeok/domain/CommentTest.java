package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {
    public static final String TEST_CONTENT = "새벽 좋아요";
    public static final String TEST_NICKNAME = "시라소니";

    // TODO: 2020/07/20 길이검증에 대한 테스트 추가 필요

    @DisplayName("Comment를 생성할 때 값들이 정상적으로 생성되야 한다")
    @Test
    void createCommentTest() {
        Member member = new Member();
        Article article = new Article();

        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            nickname(TEST_NICKNAME).
            isDeleted(false).
            build();

        assertThat(comment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(comment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(comment.getIsDeleted()).isFalse();
    }

}
