package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {
    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        this.article = new Article(1L, "내용", member, LocalDateTime.now(), true, false, new ArrayList<>());
    }

    @DisplayName("특정 사용자가 해당 게시물을 공감했는지 여부를 확인할 수 있다")
    @Test
    void isLikedByTest() {
        ArticleLike like = new ArticleLike(member, article);
        article.getLikes().add(like);

        Member anotherMember = new Member(2L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());

        assertThat(article.isLikedBy(member)).isTrue();
        assertThat(article.isLikedBy(anotherMember)).isFalse();
    }

    @DisplayName("본인의 게시글일때 true를 반환해야 한다.")
    @Test
    void isWrittenByTest() {
        assertThat(article.isWrittenBy(member)).isTrue();
    }
}