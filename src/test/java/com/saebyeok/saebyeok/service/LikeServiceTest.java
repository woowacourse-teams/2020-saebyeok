package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleLikeRepository;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    public static final long ARTICLE_ID = 1L;
    public static final long INVALID_ARTICLE_ID = 100L;

    private LikeService likeService;

    @Mock
    private ArticleLikeRepository articleLikeRepository;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        this.likeService = new LikeService(articleLikeRepository, articleRepository);
    }

    @DisplayName("게시물에 공감 등록 메서드를 실행하면 공감 등록을 수행한다")
    @Test
    void likeArticle() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(ARTICLE_ID), any())).thenReturn(Optional.of(new Article()));

        likeService.likeArticle(new Member(), ARTICLE_ID);

        verify(articleLikeRepository).save(any());
    }

    @DisplayName("예외 테스트: 잘못된 게시물에 공감 등록을 요청하면 예외가 발생한다")
    @Test
    void likeInvalidArticle() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(eq(INVALID_ARTICLE_ID), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likeService.likeArticle(new Member(), INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");

        verify(articleLikeRepository, never()).save(any());
    }


}