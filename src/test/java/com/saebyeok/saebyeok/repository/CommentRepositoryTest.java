package com.saebyeok.saebyeok.repository;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.saebyeok.saebyeok.domain.CommentTest.TEST_CONTENT;
import static com.saebyeok.saebyeok.domain.CommentTest.TEST_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        member = new Member();

        articleRepository.save(article);
        memberRepository.save(member);
    }

    private Comment createTestComment() {
        return Comment.builder().
            content(TEST_CONTENT).
            nickname(TEST_NICKNAME).
            isDeleted(false).
            build();
    }

    @DisplayName("댓글을 DB에 저장하고, 저장된 댓글을 확인한다")
    @Test
    void saveCommentTest() {
        //given
        Comment comment = createTestComment();

        //when
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAll();
        Long id = comments.get(0).getId();
        Comment savedComment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));

        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(savedComment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(savedComment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(savedComment.getIsDeleted()).isFalse();
    }

    @DisplayName("댓글 여러개를 DB에 저장하고, 저장된 댓글들을 조회한다")
    @Test
    void findCommentsTest() {
        //given
        Comment comment1 = createTestComment();
        Comment comment2 = createTestComment();
        Comment comment3 = createTestComment();

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        List<Comment> comments = commentRepository.findAll();

        //then
        assertThat(comments.size()).isEqualTo(3);

        assertThat(comments.get(0).getId()).isEqualTo(comment1.getId());
        assertThat(comments.get(0).getContent()).isEqualTo(comment1.getContent());
        assertThat(comments.get(0).getNickname()).isEqualTo(comment1.getNickname());

        assertThat(comments.get(1).getId()).isEqualTo(comment2.getId());
        assertThat(comments.get(1).getContent()).isEqualTo(comment2.getContent());
        assertThat(comments.get(1).getNickname()).isEqualTo(comment2.getNickname());

        assertThat(comments.get(2).getId()).isEqualTo(comment3.getId());
        assertThat(comments.get(2).getContent()).isEqualTo(comment3.getContent());
        assertThat(comments.get(2).getNickname()).isEqualTo(comment3.getNickname());
    }

    @DisplayName("존재하지 않는 댓글을 조회할 경우, 빈 값을 반환한다")
    @Test
    void findCommentExceptionTest() {
        //given
        Long notExistCommentId = 1L;
        //when
        //then
        assertThat(commentRepository.findById(notExistCommentId)).isNotPresent();
    }

    @DisplayName("댓글을 DB에서 삭제하고, 삭제한 댓글이 조회되지 않는다")
    @Test
    void deleteCommentTest() {
        //given
        Comment comment = createTestComment();

        commentRepository.save(comment);
        Long deletedCommentId = comment.getId();

        //when
        commentRepository.delete(comment);

        //then
        assertThat(commentRepository.findById(deletedCommentId)).isNotPresent();
    }

    @DisplayName("예외 테스트: 존재하지 않는 댓글을 삭제할 경우, 예외가 발생한다")
    @Test
    void deleteCommentExceptionTest() {
        //given
        Long notExistCommentId = 1L;
        //when
        //then
        assertThatThrownBy(() -> commentRepository.deleteById(notExistCommentId)).
            isInstanceOf(EmptyResultDataAccessException.class);
    }
}
