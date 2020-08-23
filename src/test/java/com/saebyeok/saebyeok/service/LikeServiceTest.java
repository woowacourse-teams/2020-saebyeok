package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import com.saebyeok.saebyeok.exception.DuplicateCommentLikeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    public static final long ARTICLE_ID = 1L;
    public static final long INVALID_ARTICLE_ID = 100L;
    public static final long ALREADY_LIKED_ARTICLE_ID = 1L;
    public static final long COMMENT_ID = 1L;
    public static final long INVALID_COMMENT_ID = 100L;
    public static final long ALREADY_LIKED_COMMENT_ID = 1L;

    private LikeService likeService;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @Mock
    private CommentRepository commentRepository;

    private Member member;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        this.likeService = new LikeService(articleLikeRepository, articleRepository, commentLikeRepository, commentRepository);
        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article = new Article(ARTICLE_ID, "내용", member, LocalDateTime.now(), false, Collections.emptyList(), new ArrayList<>());
        this.comment = new Comment(1L, "내용", member, "익명1", LocalDateTime.now(), article, false, new ArrayList<>());
    }

    @DisplayName("게시물 공감 등록 메서드를 실행하면 공감 등록을 수행한다")
    @Test
    void likeArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ARTICLE_ID), any())).thenReturn(Optional.of(article));
        when(articleLikeRepository.save(any())).thenReturn(new ArticleLike(member, article));

        likeService.likeArticle(new Member(), ARTICLE_ID);

        verify(articleLikeRepository).save(any());
        assertThat(article.getLikes()).isNotEmpty();
    }

    @DisplayName("예외 테스트: 잘못된 게시물에 공감 등록을 요청하면 예외가 발생한다")
    @Test
    void likeInvalidArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(INVALID_ARTICLE_ID), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeArticle(member, INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");

        verify(articleLikeRepository, never()).save(any());
    }

    @DisplayName("예외 테스트: 이미 공감한 게시물에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void likeArticleAgainTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ALREADY_LIKED_ARTICLE_ID), any())).thenReturn(Optional.of(article));
        when(articleLikeRepository.save(any(ArticleLike.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> likeService.likeArticle(member, ALREADY_LIKED_ARTICLE_ID))
                .isInstanceOf(DuplicateArticleLikeException.class)
                .hasMessage("이미 공감한 게시물에 추가 공감을 할 수 없습니다. MemberId: " + member.getId() + ", articleId: " + ALREADY_LIKED_ARTICLE_ID);
    }

    @DisplayName("게시글 공감 취소 메서드를 실행하면 공감이 삭제된다")
    @Test
    void unlikeArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ARTICLE_ID), any())).thenReturn(Optional.of(article));

        likeService.unlikeArticle(member, ARTICLE_ID);

        verify(articleLikeRepository).deleteByMemberAndArticle(member, article);
    }

    @DisplayName("예외 테스트: 잘못된 게시물에 공감 취소를 요청하면 예외가 발생한다")
    @Test
    void unlikeInvalidArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(INVALID_ARTICLE_ID), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeArticle(member, INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");

        verify(articleLikeRepository, never()).deleteByMemberAndArticle(any(), any());
    }

    @DisplayName("댓글 공감 등록 메서드를 실행하면 공감 등록을 수행한다")
    @Test
    void likeCommentTest() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        likeService.likeComment(new Member(), COMMENT_ID);

        verify(commentLikeRepository).save(any());
        assertThat(comment.getLikes()).isNotEmpty();
    }

    @DisplayName("예외 테스트: 잘못된 댓글에 공감 등록을 요청하면 예외가 발생한다")
    @Test
    void likeInvalidCommentTest() {
        when(commentRepository.findById(INVALID_COMMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeComment(member, INVALID_COMMENT_ID))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage(INVALID_COMMENT_ID + "에 해당하는 댓글을 찾을 수 없습니다!");

        verify(commentLikeRepository, never()).save(any());
    }

    @DisplayName("예외 테스트: 이미 공감한 댓글에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void likeCommentAgainTest() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));
        when(commentLikeRepository.save(any(CommentLike.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> likeService.likeComment(member, ALREADY_LIKED_COMMENT_ID))
                .isInstanceOf(DuplicateCommentLikeException.class)
                .hasMessage("이미 공감한 댓글에 추가 공감을 할 수 없습니다. MemberId: " + member.getId() + ", commentId: " + ALREADY_LIKED_COMMENT_ID);
    }


    @DisplayName("댓글 공감 취소 메서드를 실행하면 공감이 삭제된다")
    @Test
    void unlikeCommentTest() {
        when(commentRepository.findById(COMMENT_ID)).thenReturn(Optional.of(comment));

        likeService.unlikeComment(member, COMMENT_ID);

        verify(commentLikeRepository).deleteByMemberAndComment(member, comment);
    }

    @DisplayName("예외 테스트: 잘못된 댓글에 공감 취소를 요청하면 예외가 발생한다")
    @Test
    void unlikeInvalidCommentTest() {
        when(commentRepository.findById(INVALID_COMMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeComment(member, INVALID_COMMENT_ID))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessage(INVALID_COMMENT_ID + "에 해당하는 댓글을 찾을 수 없습니다!");

        verify(commentLikeRepository, never()).deleteByMemberAndComment(any(), any());
    }
}