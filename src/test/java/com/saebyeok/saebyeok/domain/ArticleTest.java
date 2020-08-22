package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {

    @DisplayName("본인의 게시글일때 true를 반환해야 한다.")
    @Test
    void isWrittenByTest() {
        Member member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        Article article = new Article(1L, "내용", member, LocalDateTime.now(), true, null, 0L);
        article.setMember(member);

        assertThat(article.isWrittenBy(member)).isTrue();
    }

    @DisplayName("게시글에 이미 댓글을 남겼다면 그 댓글에 부여된 닉네임을 불러온다.")
    @Test
    void loadExistingNicknameTest() {
        Member member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        Comment comment = new Comment("내용", "닉네임1", false);
        comment.setMember(member);
        Article article = new Article(1L, "내용", member, LocalDateTime.now(), true, Arrays.asList(comment), 0L);

        assertThat(article.loadExistingNickname(member)).isEqualTo(Optional.of("닉네임1"));
    }

    @DisplayName("게시글에 남긴 댓글이 없다면 빈 Optional을 반환한다.")
    @Test
    void loadExistingNicknameTest2() {
        Member member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        Article article = new Article(1L, "내용", member, LocalDateTime.now(), true, Collections.emptyList(), 0L);

        assertThat(article.loadExistingNickname(member)).isEmpty();
    }

    @DisplayName("게시물에 등록된 모든 닉네임을 불러온다")
    @Test
    void getAllNicknamesTest() {
        Member member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        Comment comment1 = new Comment("내용1", "닉네임1", false);
        Comment comment2 = new Comment("내용2", "닉네임2", false);
        Comment comment3 = new Comment("내용3", "닉네임3", false);
        Article article = new Article(1L, "내용", member, LocalDateTime.now(), true, Arrays.asList(comment1, comment2, comment3), 0L);

        assertThat(article.getAllNicknames()).
                hasSize(3).
                containsOnly("닉네임1", "닉네임2", "닉네임3");
    }
}