package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    private Member member;

    @BeforeEach
    void setup(){
        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
    }

    @Test
    @DisplayName("사용자의 게시글을 추가하면 리스트에 추가되어야 한다")
    void addArticleTest() {
        Article article = new Article(1L, "내용", member, LocalDateTime.now(), true, false, null, new ArrayList<>());
        member.addArticle(article);

        assertThat(member.getArticles().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("회원 탈퇴를 하면 개인 정보가 삭제되고 isDeleted=true 처리 되어야 한다")
    void deactivateTest() {
        member.deactivate();

        assertThat(member.getIsDeleted()).isEqualTo(true);
        assertThat(member.getOauthId()).isEqualTo("DEACTIVATED");
        assertThat(member.getLoginMethod()).isEqualTo("DEACTIVATED");
    }
}
