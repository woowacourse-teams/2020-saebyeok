package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleLikeRepository;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    public static final long ARTICLE_ID = 1L;

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
}