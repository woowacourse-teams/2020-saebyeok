package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.InvalidLengthCommentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentTest {
    public static final String TEST_CONTENT = "새벽 좋아요";
    public static final String TEST_NICKNAME = "시라소니";
    public static final String UNDER_LENGTH_CONTENT = " ";
    public static final String OVER_LENGTH_CONTENT = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
        "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
        "1만4000원.\n";

    @DisplayName("Comment를 생성할 때 값들이 정상적으로 생성되야 한다")
    @Test
    void createCommentTest() {
        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            nickname(TEST_NICKNAME).
            isDeleted(false).
            build();

        assertThat(comment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(comment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(comment.getIsDeleted()).isFalse();
    }

    @DisplayName("예외 테스트: Comment가 정해진 최소 길이보다 작을 때 예외가 발생해야 한다")
    @Test
    void underLengthCommentTest() {
        assertThatThrownBy(() -> Comment.builder().
            content(UNDER_LENGTH_CONTENT).
            nickname(TEST_NICKNAME).
            isDeleted(false).
            build())
            .isInstanceOf(InvalidLengthCommentException.class)
            .hasMessageContaining("올바르지 않은 댓글입니다!");
    }

    @DisplayName("예외 테스트: Comment가 정해진 최대 길이보다 클 때 예외가 발생해야 한다")
    @Test
    void overLengthCommentTest() {
        assertThatThrownBy(() -> Comment.builder().
            content(OVER_LENGTH_CONTENT).
            nickname(TEST_NICKNAME).
            isDeleted(false).
            build())
            .isInstanceOf(InvalidLengthCommentException.class)
            .hasMessageContaining("올바르지 않은 댓글입니다!");
    }
}
