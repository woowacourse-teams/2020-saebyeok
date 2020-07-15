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

    @DisplayName("댓글을 DB에 저장하고, 저장된 댓글을 확인한다.")
    @Test
    void saveCommentTest() {
        Comment comment = Comment.builder().
            content(TEST_CONTENT).
            member(member).
            nickname(TEST_NICKNAME).
            article(article).
            isDeleted(false).
            build();

        commentRepository.save(comment);
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(1);

        Long id = comments.get(0).getId();
        Comment savedComment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        assertThat(savedComment.getContent()).isEqualTo(TEST_CONTENT);
        assertThat(savedComment.getMember().getId()).isEqualTo(member.getId());
        assertThat(savedComment.getNickname()).isEqualTo(TEST_NICKNAME);
        assertThat(savedComment.getArticle().getId()).isEqualTo(article.getId());
        assertThat(savedComment.getIsDeleted()).isFalse();
    }
}
