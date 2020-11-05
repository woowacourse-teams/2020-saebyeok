package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleLikeTest {

    @DisplayName("예외 테스트: Member 혹은 Article에 null이 오면 예외가 발생한다")
    @Test
    void createWithNullArgsTest() {
        Article article = new Article();
        Member member = new Member();

        assertThatThrownBy(() -> new ArticleLike(null, article)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArticleLike(member, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ArticleLike(null, null)).isInstanceOf(NullPointerException.class);
    }
}
