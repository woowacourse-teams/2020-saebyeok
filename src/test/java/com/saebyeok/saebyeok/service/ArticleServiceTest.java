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
import java.util.Collections;
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
    private static final String MEMBER_OAUTH_ID = "123456789";
    private static final String MEMBER_LOGIN_METHOD = "naver";
    private static final boolean IS_DELETED = false;
    private static final Long ARTICLE_ID_1 = 1L;
    private static final Long ARTICLE_ID_2 = 2L;
    private static final Long INVALID_ARTICLE_ID = 3L;
    private static final String CONTENT1 = "내용1";
    private static final String CONTENT2 = "내용2";
    private static final boolean IS_COMMENT_ALLOWED_1 = true;
    private static final boolean IS_COMMENT_ALLOWED_2 = false;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;
    private static final long TEST_LIKES_COUNT = 10L;

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleEmotionService articleEmotionService;

    @Mock
    private ArticleSubEmotionService articleSubEmotionService;

    private Member member;
    private Article article1;
    private Article article2;
    private Emotion emotion;
    private List<SubEmotion> subEmotions;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository, articleEmotionService, articleSubEmotionService);
        member = new Member(MEMBER_ID, MEMBER_OAUTH_ID, MEMBER_LOGIN_METHOD, LocalDateTime.now(), IS_DELETED, Role.USER, new ArrayList<>());
        article1 = new Article(ARTICLE_ID_1, CONTENT1, member, LocalDateTime.now(), IS_COMMENT_ALLOWED_1, null, TEST_LIKES_COUNT, new ArrayList<>());
        article2 = new Article(ARTICLE_ID_2, CONTENT2, member, LocalDateTime.of(2020, 6, 12, 5, 30, 0), IS_COMMENT_ALLOWED_2, null, TEST_LIKES_COUNT, new ArrayList<>());
    }

    @DisplayName("게시글을 조회하면 게시글 목록이 리턴된다")
    @Test
    void getArticlesTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(article1);
        when(articleRepository.findAllByCreatedDateGreaterThanEqual(any(), any())).thenReturn(articles);

        List<ArticleResponse> articleResponses = articleService.getArticles(member, PAGE_NUMBER, PAGE_SIZE, Collections.emptyList());

        assertThat(articleResponses).hasSize(1);
        assertThat(articleResponses.get(0).getContent()).isEqualTo(CONTENT1);
        assertThat(articleResponses.get(0).getIsCommentAllowed()).isTrue();
        assertThat(articleResponses.get(0).getLikesCount()).isNotNull();
        assertThat(articleResponses.get(0).getIsLikedByMe()).isNotNull();
    }

    @DisplayName("ID로 개별 글 조회를 요청하면 해당 글을 전달 받는다")
    @Test
    void readArticleTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(any(), any())).thenReturn(Optional.of(article1));


        ArticleResponse articleResponse = articleService.readArticle(member, ARTICLE_ID_1);

        assertThat(articleResponse).isNotNull();
        assertThat(articleResponse.getContent()).isEqualTo(CONTENT1);
        assertThat(articleResponse.getIsCommentAllowed()).isTrue();
        assertThat(articleResponse.getLikesCount()).isNotNull();
        assertThat(articleResponse.getIsLikedByMe()).isNotNull();
    }

    @DisplayName("예외 테스트: 요청에 해당하는 ID가 없으면 ArticleNotFoundException이 발생한다")
    @Test
    void readArticleExceptionTest() {
        when(articleRepository.findByIdAndCreatedDateGreaterThanEqual(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleService.readArticle(member, INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 ID의 글 삭제를 요청하면 해당 글을 삭제한다")
    @Test
    void deleteArticleTest() throws IllegalAccessException {
        when(articleRepository.findById(any())).thenReturn(Optional.of(article1));
        articleService.deleteArticle(member, ARTICLE_ID_1);

        verify(articleRepository).deleteById(any());
    }

    @DisplayName("예외 테스트: 잘못된 ID의 글 삭제를 요청하면 오류를 발생시킨다")
    @Test
    void deleteArticleExceptionTest() {
        doThrow(new ArticleNotFoundException(INVALID_ARTICLE_ID))
                .when(articleRepository).findById(INVALID_ARTICLE_ID);

        assertThatThrownBy(() -> articleService.deleteArticle(member, INVALID_ARTICLE_ID))
                .isInstanceOf(ArticleNotFoundException.class)
                .hasMessage(INVALID_ARTICLE_ID + "에 해당하는 게시글을 찾을 수 없습니다.");
    }

    @DisplayName("내 게시글 목록을 조회하면 게시글 목록이 작성 시간에 관계없이 리턴된다")
    @Test
    void getMemberArticlesTest() {
        List<Article> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);
        when(articleRepository.findAllByMember(eq(member), any())).thenReturn(articles);


        List<ArticleResponse> articleResponses = articleService.getMemberArticles(member, PAGE_NUMBER, PAGE_SIZE, Collections.emptyList());

        assertThat(articleResponses).hasSize(2);
        assertThat(articleResponses.get(0).getContent()).isEqualTo(CONTENT1);
        assertThat(articleResponses.get(0).getIsCommentAllowed()).isTrue();
        assertThat(articleResponses.get(0).getLikesCount()).isNotNull();
        assertThat(articleResponses.get(0).getIsLikedByMe()).isNotNull();
        assertThat(articleResponses.get(1).getContent()).isEqualTo(CONTENT2);
        assertThat(articleResponses.get(1).getIsCommentAllowed()).isFalse();
        assertThat(articleResponses.get(1).getLikesCount()).isNotNull();
        assertThat(articleResponses.get(1).getIsLikedByMe()).isNotNull();
    }

    @DisplayName("ID로 내가 쓴 개별 글 조회를 요청하면 만료 시간과 상관없이 해당 글을 전달 받는다")
    @Test
    void readMemberArticleTest() {
        when(articleRepository.findByIdAndMemberEquals(any(), any())).thenReturn(Optional.of(article2));

        ArticleResponse articleResponse = articleService.readMemberArticle(member, ARTICLE_ID_2);

        assertThat(articleResponse).isNotNull();
        assertThat(articleResponse.getContent()).isEqualTo(CONTENT2);
        assertThat(articleResponse.getIsCommentAllowed()).isFalse();
        assertThat(articleResponse.getLikesCount()).isNotNull();
        assertThat(articleResponse.getIsLikedByMe()).isNotNull();
    }
}
