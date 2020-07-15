package com.saebyeok.saebyeok.repository;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// TODO: 2020/07/15 테스트 코드 나중에 수정하기(모든 pulbic 제거하기)
@SpringBootTest
public class CommentRepositoryTest {
    private static final String TEST_CONTENT = "새벽 좋아요";
    private static final String TEST_NICKNAME = "시라소니";

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

    @AfterEach
    public void cleanup() {
        commentRepository.deleteAll();
    }

    @DisplayName("댓글을 DB에 저장하고, 저장된 댓글을 확인한다")
    @Test
    void saveCommentTest() {
        //given
        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();

        //when
        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAll();
        Long id = comments.get(0).getId();
        Comment savedComment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);

        //then
        assertThat(comments.size()).isEqualTo(1);
        assertThat(savedComment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(savedComment.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedComment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(savedComment.getArticle().getId()).isEqualTo(article.getId());
        assertThat(savedComment.getIsDeleted()).isFalse();
    }

    @DisplayName("댓글 여러개를 DB에 저장하고, 저장된 댓글들을 조회한다")
    @Test
    void findCommentsTest() {
        //given
        Comment comment1 = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();
        Comment comment2 = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();
        Comment comment3 = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();

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

    @DisplayName("댓글을 DB에서 삭제하고, 삭제한 댓글이 조회되지 않는다")
    @Test
    void deleteCommentTest() {
        //given
        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();

        commentRepository.save(comment);
        Long id = comment.getId();

        //when
        commentRepository.delete(comment);

        //then
        assertThatThrownBy(() -> commentRepository.findById(id).
            orElseThrow(CommentNotFoundException::new)).
            isInstanceOf(CommentNotFoundException.class).
            hasMessage("해당 댓글을 찾을 수 없습니다!");
    }
}
