package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    private static final Long MEMBER_ID = 1L;
    private static final int BIRTH_YEAR = 1996;
    private static final boolean IS_DELETED = false;
    private static final Long ARTICLE_ID = 1L;
    private static final Long INVALID_ARTICLE_ID = 2L;
    private static final String CONTENT = "내용";
    private static final boolean IS_COMMENT_ALLOWED = true;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleEmotionService articleEmotionService;

    @Mock
    private ArticleSubEmotionService articleSubEmotionService;

    private Member member;
    private Article article;
    private Emotion emotion;
    private List<SubEmotion> subEmotions;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, articleEmotionService, articleSubEmotionService);
        member = new Member(MEMBER_ID, BIRTH_YEAR, Gender.MALE, LocalDateTime.now(), IS_DELETED, null);

        article = new Article(CONTENT, IS_COMMENT_ALLOWED);
    }

    @DisplayName("게시글을 조회하면 게시글 목록이 리턴된다")
    @Test
    void getArticlesTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(article);
        when(articleRepository.findAllByCreatedDateGreaterThanEqual(any(), any())).thenReturn(articles);

        List<ArticleResponse> articleResponses = articleService.getArticles(PAGE_NUMBER, PAGE_SIZE);

        assertThat(articleResponses).hasSize(1);
        assertThat(articleResponses.get(0).getContent()).isEqualTo(CONTENT);
        assertThat(articleResponses.get(0).getIsCommentAllowed()).isTrue();
    }

    @DisplayName("ID로 개별 글 조회를 요청하면 해당 글을 전달 받는다")
    @Test
    void readArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(any(), any())).thenReturn(Optional.of(article));

        ArticleResponse articleResponse = articleService.readArticle(ARTICLE_ID);

        assertThat(articleResponse).isNotNull();
        assertThat(articleResponse.getContent()).isEqualTo(CONTENT);
        assertThat(articleResponse.getIsCommentAllowed()).isTrue();
    }

    @DisplayName("예외 테스트: 요청에 해당하는 ID가 없으면 ArticleNotFoundException이 발생한다")
    @Test
    void readArticleExceptionTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.readArticle(INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 ID의 글 삭제를 요청하면 해당 글을 삭제한다")
    @Test
    void deleteArticleTest() {
        when(articleRepository.findById(any())).thenReturn(Optional.of(article));

        articleService.deleteArticle(ARTICLE_ID);

        verify(articleRepository).deleteById(any());
    }

    @DisplayName("예외 테스트: 잘못된 ID의 글 삭제를 요청하면 오류를 발생시킨다")
    @Test
    void deleteArticleExceptionTest() {
        doThrow(new ArticleNotFoundException(INVALID_ARTICLE_ID))
                .when(articleRepository).findById(INVALID_ARTICLE_ID);

        assertThatThrownBy(() -> articleService.deleteArticle(INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");
    }
}
