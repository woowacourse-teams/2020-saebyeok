package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    @DisplayName("Comment를 생성할 때 값들이 정상적으로 생성되야 한다.")
    @Test
    void createCommentTest() {
        Member member = new Member();
        LocalDateTime now = LocalDateTime.now();
        Article article = new Article();

        Comment comment = Comment.builder()
            .content("새벽 좋아요")
            .member(member)
            .nickname("시라소니")
            .createdDate(now)
            .article(article)
            .isDeleted(false)
            .build();

        assertThat(comment.getContent()).isEqualTo("새벽 좋아요");
        assertThat(comment.getMember()).isEqualTo(member);
        assertThat(comment.getNickname()).isEqualTo("시라소니");
        assertThat(comment.getCreatedDate()).isEqualTo(now);
        assertThat(comment.getArticle()).isEqualTo(article);
        assertThat(comment.getIsDeleted()).isFalse();
    }

}
