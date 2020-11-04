package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {
    private Member member;
    private AnalysisService analysisService;

    @Mock
    private ArticleService articleService;

    @Mock
    private EmotionService emotionService;

    @Mock
    private ArticleEmotionService articleEmotionService;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        analysisService = new AnalysisService(articleService, emotionService, articleEmotionService, commentService);

        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(),
                                 false, Role.USER, new ArrayList<>());
    }

    @DisplayName("Member가 작성한 Article에 대한 Emotion별 통계를 받아온다")
    @Test
    void getArticlesAnalysisTest() {
        List<Integer> articleEmotionsCount = Arrays.asList(2, 1, 1, 0, 3, 0);
        Long mostEmotionId = 5L;
        List<Article> memberArticles = Arrays.asList(new Article());
        List<Long> allEmotionsIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L);

        when(emotionService.getAllEmotionsIds()).thenReturn(allEmotionsIds);
        when(articleService.getVisibleDaysArticles(any(Member.class), anyInt())).thenReturn(memberArticles);
        when(articleEmotionService.findArticleEmotionsCount(memberArticles, allEmotionsIds)).thenReturn(articleEmotionsCount);
        when(articleEmotionService.findMostEmotionIdInArticles(memberArticles)).thenReturn(mostEmotionId);

        ArticlesAnalysisResponse result = analysisService.getArticlesAnalysis(member);

        assertThat(result.getArticleEmotionsCount()).
                hasSize(6).
                containsExactly(articleEmotionsCount.get(0), articleEmotionsCount.get(1), articleEmotionsCount.get(2),
                        articleEmotionsCount.get(3), articleEmotionsCount.get(4), articleEmotionsCount.get(5));
        assertThat(result.getMostEmotionId()).isEqualTo(mostEmotionId);
    }

    @DisplayName("Member가 작성한 Comment의 개수와 Likes 개수에 대한 값을 받아온다")
    @Test
    void countTotalCommentsByTest() {
        Long totalCommentsCount = 1L;
        Long totalCommentsLikesCount = 4L;
        List<CommentLike> likes = new ArrayList<>(Arrays.asList(new CommentLike(), new CommentLike()));
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, "content1", new Member(), "nickname1", LocalDateTime.now(), new Article(), false, null, likes));
        comments.add(new Comment(2L, "content2", new Member(), "nickname2", LocalDateTime.now(), new Article(), false, null, likes));

        when(commentService.countTotalCommentsBy(any(Member.class))).thenReturn(totalCommentsCount);
        when(commentService.findAllCommentsBy(any(Member.class))).thenReturn(comments);

        CommentsAnalysisResponse result = analysisService.getCommentsAnalysis(member);

        assertThat(result.getTotalCommentsCount()).isEqualTo(totalCommentsCount);
        assertThat(result.getTotalCommentLikesCount()).isEqualTo(totalCommentsLikesCount);
    }
}
