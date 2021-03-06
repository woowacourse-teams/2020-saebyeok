package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.saebyeok.saebyeok.domain.CommentTest.TEST_CONTENT;
import static com.saebyeok.saebyeok.domain.CommentTest.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Sql("/truncate.sql")
@Transactional
@SpringBootTest
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Article article;
    private Member member;

    @BeforeEach
    void setUp() {
        article = new Article();
        member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());

        articleRepository.save(article);
        memberRepository.save(member);
    }

    private Comment createTestComment() {
        return Comment.builder().
                content(TEST_CONTENT).
                member(member).
                nickname(TEST_NICKNAME).
                article(article).
                parent(null).
                build();
    }

    @DisplayName("댓글을 DB에 저장하고, 저장된 댓글을 확인한다")
    @Test
    void saveCommentTest() {
        Comment comment = createTestComment();

        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAll();
        Long id = comments.get(0).getId();
        Comment savedComment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));

        assertThat(comments.size()).isEqualTo(1);
        assertThat(savedComment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(savedComment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(savedComment.getIsDeleted()).isFalse();
    }

    @DisplayName("댓글 여러개를 DB에 저장하고, 저장된 댓글들을 조회한다")
    @Test
    void findCommentsTest() {
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        List<Comment> comments = commentRepository.findAll();

        assertThat(comments).
                hasSize(3).
                extracting("id").
                containsOnly(comment1.getId(), comment2.getId(), comment3.getId());
    }

    @DisplayName("댓글 여러개를 DB에 저장하고, 저장된 댓글들을 findAllByArticleId로 조회한다")
    @Test
    void findAllByArticleIdTest() {
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        List<Comment> comments = commentRepository.findAllByArticleId(article.getId());

        assertThat(comments).
                hasSize(3).
                extracting("id").
                containsOnly(comment1.getId(), comment2.getId(), comment3.getId());
    }

    @DisplayName("댓글 여러개를 DB에 저장하고, 저장된 댓글의 개수를 countCommentsByArticleId로 조회한다")
    @Test
    void countCommentsByArticleIdTest() {
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        Long commentsSize = commentRepository.countCommentsByArticleId(article.getId());

        assertThat(commentsSize).isEqualTo(3L);
    }

    @DisplayName("존재하지 않는 댓글을 조회할 경우, 빈 값을 반환한다")
    @Test
    void findCommentExceptionTest() {
        Long notExistCommentId = 1L;

        assertThat(commentRepository.findById(notExistCommentId)).isNotPresent();
    }

    @DisplayName("댓글을 DB에서 삭제하고, 삭제한 댓글이 조회되지 않는다")
    @Test
    void deleteCommentTest() {
        Comment comment = createTestComment();

        commentRepository.save(comment);
        Long deletedCommentId = comment.getId();

        commentRepository.delete(comment);

        assertThat(commentRepository.findById(deletedCommentId)).isNotPresent();
    }

    @DisplayName("예외 테스트: 존재하지 않는 댓글을 삭제할 경우, 예외가 발생한다")
    @Test
    void deleteCommentExceptionTest() {
        Long notExistCommentId = 1L;

        assertThatThrownBy(() -> commentRepository.deleteById(notExistCommentId)).
                isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("Member가 작성한 댓글 중 삭제하지 않은 댓글의 개수를 정확히 반환한다.")
    @Test
    void countTotalCommentsByMemberTest() {
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        Long totalCommentsCount = commentRepository.countCommentsByMemberAndIsDeleted(member, false);

        assertThat(totalCommentsCount).isEqualTo(3L);
    }

    @DisplayName("Member가 작성한 댓글 중 삭제하지 않은 댓글을 모두 가져온다.")
    @Test
    void findAllCommentsByMemberTest() {
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        List<Comment> comments = commentRepository.findAllByMemberAndIsDeleted(member, false);

        assertThat(comments).hasSize(3);
        assertThat(comments.get(0).getId()).isEqualTo(comment1.getId());
        assertThat(comments.get(1).getContent()).isEqualTo(comment1.getContent());
        assertThat(comments.get(2).getNickname()).isEqualTo(comment1.getNickname());
    }
}
