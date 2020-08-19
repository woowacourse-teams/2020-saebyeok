package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    public static final long ARTICLE_ID = 1L;
    public static final long INVALID_ARTICLE_ID = 100L;
    public static final long ALREADY_LIKED_ARTICLE_ID = 1L;

    private LikeService likeService;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleRepository articleRepository;

    private Member member;
    private Article article;

    @BeforeEach
    void setUp() {
        this.likeService = new LikeService(articleLikeRepository, articleRepository);
        this.member = new Member(1L, "a@a.com", 1991, Gender.FEMALE, LocalDateTime.now(), false, Role.USER, Collections.emptyList());
        this.article = new Article(ARTICLE_ID, "내용", member, LocalDateTime.now(), false, Collections.emptyList());
    }

    @DisplayName("게시물에 공감 등록 메서드를 실행하면 공감 등록을 수행한다")
    @Test
    void likeArticle() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ARTICLE_ID), any())).thenReturn(Optional.of(article));

        likeService.likeArticle(new Member(), ARTICLE_ID);

        verify(articleLikeRepository).save(any());
    }

    @DisplayName("예외 테스트: 잘못된 게시물에 공감 등록을 요청하면 예외가 발생한다")
    @Test
    void likeInvalidArticle() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(INVALID_ARTICLE_ID), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeArticle(member, INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");

        verify(articleLikeRepository, never()).save(any());
    }

    @DisplayName("예외 테스트: 이미 공감한 게시물에 다시 공감을 요청하면 예외가 발생한다")
    @Test
    void likeArticleAgain() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ALREADY_LIKED_ARTICLE_ID), any())).thenReturn(Optional.of(article));
        when(articleLikeRepository.save(any(ArticleLike.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> likeService.likeArticle(member, ALREADY_LIKED_ARTICLE_ID))
                .isInstanceOf(DuplicateArticleLikeException.class)
                .hasMessage("이미 공감한 게시물에 추가 공감을 할 수 없습니다. MemberId: " + member.getId() + "articleId" + ALREADY_LIKED_ARTICLE_ID);
    }
}