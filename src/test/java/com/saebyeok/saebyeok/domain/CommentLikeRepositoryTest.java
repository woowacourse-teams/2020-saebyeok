package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;
    private Member member;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "a@a.com", 1991, Gender.FEMALE, LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article = new Article(1L, "내용", member, LocalDateTime.now(), false, Collections.emptyList());
        ;
        this.comment = new Comment(1L, "내용", member, "익명1", LocalDateTime.now(), article, false);
    }

    @DisplayName("댓글에 공감을 등록할 수 있다")
    @Test
    void saveCommentLike() {
        CommentLike like = new CommentLike(member, comment);

        CommentLike savedLike = commentLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike).isEqualTo(like);
        assertThat(savedLike.getId()).isNotNull();
    }

    @DisplayName("예외 테스트: Member와 Comment 참조가 없이 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveArticleLikeWithoutMemberAndArticle() {
        CommentLike like = new CommentLike();

        assertThatThrownBy(() -> commentLikeRepository.save(like))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외 테스트: 참조할 수 없는 Member 혹은 Article 로 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveArticleLikeWithInvalidMemberOrArticle() {
        CommentLike likeWithInvalidMember = new CommentLike(new Member(), comment);
        CommentLike likeWithInvalidArticle = new CommentLike(member, new Comment());

        assertThatThrownBy(() -> commentLikeRepository.save(likeWithInvalidMember))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThatThrownBy(() -> commentLikeRepository.save(likeWithInvalidArticle))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}