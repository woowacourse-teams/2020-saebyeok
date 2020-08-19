package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;

    @DisplayName("댓글에 공감을 등록할 수 있다")
    @Test
    void saveCommentLike() {
        CommentLike like = new CommentLike(new Member(), new Comment());

        CommentLike savedLike = commentLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike).isEqualTo(like);
        assertThat(savedLike.getId()).isNotNegative();
    }
}