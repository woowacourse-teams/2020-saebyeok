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
class ArticleLikeRepositoryTest {

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @DisplayName("게시물에 공감을 등록할 수 있다")
    @Test
    void saveArticleLike() {
        ArticleLike like = new ArticleLike(new Member(), new Article());

        ArticleLike savedLike = articleLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike).isEqualTo(like);
        assertThat(savedLike.getId()).isNotNegative();
    }
}