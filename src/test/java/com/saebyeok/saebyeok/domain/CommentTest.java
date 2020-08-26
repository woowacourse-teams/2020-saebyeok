package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.DuplicateCommentLikeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentTest {
    public static final String TEST_CONTENT = "새벽 좋아요";
    public static final String TEST_NICKNAME = "시라소니";
    public static final String UNDER_LENGTH_CONTENT = " ";
    public static final String OVER_LENGTH_CONTENT = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
            "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
            "1만4000원.\n";

    private Member member;
    private Comment comment1;
    private Comment comment2;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.comment1 = new Comment(1L, "내용", member, "익명1", LocalDateTime.now(), new Article(), false, new ArrayList<>());
        this.comment2 = new Comment(2L, "내용", member, "익명2", LocalDateTime.now(), new Article(), false, new ArrayList<>());
    }

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

    @DisplayName("예외 테스트: 해당 댓글에 대한 CommentLike가 아닌 CommentLike를 추가하면 예외가 발생한다")
    @Test
    void addInvalidLikeExceptionTest() {
        CommentLike invalidCommentLike = new CommentLike(member, comment2);

        assertThatThrownBy(() -> comment1.addLike(invalidCommentLike))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("예외 테스트: 이미 존재하는 CommentLike를 추가하면 예외가 발생한다")
    @Test
    void addLikeExceptionTest() {
        CommentLike commentLike = new CommentLike(member, comment1);
        comment1.addLike(commentLike);

        assertThatThrownBy(() -> comment1.addLike(commentLike))
                .isInstanceOf(DuplicateCommentLikeException.class)
                .hasMessage("이미 공감한 댓글에 추가 공감을 할 수 없습니다. MemberId: " + member.getId() + ", commentId: " + comment1.getId()); }

    @DisplayName("특정 사용자가 해당 댓글을 공감했는지 여부를 확인할 수 있다")
    @Test
    void isLikedByTest() {
        CommentLike like = new CommentLike(member, comment1);
        comment1.getLikes().add(like);

        Member anotherMember = new Member(2L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());

        assertThat(comment1.isLikedBy(member)).isTrue();
        assertThat(comment1.isLikedBy(anotherMember)).isFalse();
    }
}
