package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ArticleTest {
    private Member member;
    private Article article1;
    private Article article2;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article1 = new Article(1L, "내용", member, LocalDateTime.now(), false, Collections.emptyList(), new ArrayList<>());
        this.article2 = new Article(2L, "내용", member, LocalDateTime.now(), false, Collections.emptyList(), new ArrayList<>());
    }

    @DisplayName("예외 테스트: 해당 게시물에 대한 ArticleLike가 아닌 ArticleLike를 추가하면 예외가 발생한다")
    @Test
    void addInvalidLikeExceptionTest() {
        ArticleLike invalidArticleLike = new ArticleLike(member, article2);

        assertThatThrownBy(() -> article1.addLike(invalidArticleLike))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("예외 테스트: 이미 존재하는 ArticleLike를 추가하면 예외가 발생한다")
    @Test
    void addLikeExceptionTest() {
        ArticleLike articleLike = new ArticleLike(member, article1);
        article1.addLike(articleLike);

        assertThatThrownBy(() -> article1.addLike(articleLike))
                .isInstanceOf(DuplicateArticleLikeException.class)
                .hasMessage("이미 공감한 게시물에 추가 공감을 할 수 없습니다. MemberId: " + member.getId() + ", articleId: " + article1.getId());
    }

    @DisplayName("특정 사용자가 해당 게시물을 공감했는지 여부를 확인할 수 있다")
    @Test
    void isLikedByTest() {
        ArticleLike like = new ArticleLike(member, article1);
        article1.addLike(like);

        Member anotherMember = new Member(2L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());

        assertThat(article1.isLikedBy(member)).isTrue();
        assertThat(article1.isLikedBy(anotherMember)).isFalse();
    }
}
