package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@Sql("/truncate.sql")
@SpringBootTest
class CommentLikeRepositoryTest {

    @Autowired
    private CommentLikeRepository commentLikeRepository;
    private Member member;
    private Article article;
    private Comment comment;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article = new Article(1L, "내용", member, LocalDateTime.now(), false, false, Collections.emptyList(), Collections.emptyList());
        this.comment = new Comment(1L, "내용", member, "익명1", LocalDateTime.now(), article, false, null, Collections.emptyList());

        memberRepository.save(member);
        articleRepository.save(article);
        commentRepository.save(comment);
    }

    @DisplayName("댓글에 공감을 등록할 수 있다")
    @Test
    void saveCommentLikeTest() {
        CommentLike like = new CommentLike(member, comment);

        CommentLike savedLike = commentLikeRepository.save(like);

        assertThat(savedLike).isNotNull();
        assertThat(savedLike).isEqualTo(like);
        assertThat(savedLike.getId()).isNotNull();
    }

    @DisplayName("예외 테스트: Member와 Comment 참조가 없이 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveCommentLikeWithoutMemberAndCommentTest() {
        CommentLike like = new CommentLike();

        assertThatThrownBy(() -> commentLikeRepository.save(like))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외 테스트: 참조할 수 없는 Member 혹은 Comment 로 공감 등록을 하면 예외가 발생한다")
    @Test
    void saveCommentLikeWithInvalidMemberOrCommentTest() {
        CommentLike likeWithInvalidMember = new CommentLike(new Member(), comment);
        CommentLike likeWithInvalidComment = new CommentLike(member, new Comment());

        assertThatThrownBy(() -> commentLikeRepository.save(likeWithInvalidMember))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThatThrownBy(() -> commentLikeRepository.save(likeWithInvalidComment))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @DisplayName("예외 테스트: 이미 공감한 댓글에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void saveCommentLikeWithExistingCommentLikeTest() {
        CommentLike like = new CommentLike(member, comment);
        CommentLike likeAgain = new CommentLike(member, comment);
        commentLikeRepository.save(like);

        assertThatThrownBy(() -> commentLikeRepository.save(likeAgain))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("댓글 공감을 취소할 수 있다")
    @Test
    void deleteCommentLikeTest() {
        CommentLike like = new CommentLike(member, comment);
        commentLikeRepository.save(like);
        List<CommentLike> beforeDelete = commentLikeRepository.findAll();

        commentLikeRepository.deleteByMemberAndComment(member, comment);

        List<CommentLike> afterDelete = commentLikeRepository.findAll();

        assertThat(afterDelete.size()).isEqualTo(beforeDelete.size() - 1);
    }
}