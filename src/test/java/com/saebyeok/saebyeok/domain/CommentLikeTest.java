package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentLikeTest {

    @DisplayName("예외 테스트: Member 혹은 Comment에 null이 오면 예외가 발생한")
    @Test
    void createWithNullArgsTest() {
        Comment comment = new Comment();
        Member member = new Member();

        assertThatThrownBy(() -> new CommentLike(null, comment)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CommentLike(member, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new CommentLike(null, null)).isInstanceOf(NullPointerException.class);
    }
}