package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.saebyeok.saebyeok.service.ArticleEmotionService.NOT_EXIST_MOST_EMOTION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class AnalysisServiceTest {
    private Member member;
    private AnalysisService analysisService;

    @Mock
    private EmotionService emotionService;

    @Mock
    private ArticleEmotionService articleEmotionService;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        analysisService = new AnalysisService(emotionService, articleEmotionService, commentService);

        this.member = new Member(1L, "123456789", "naver", LocalDateTime.now(),
                                 false, Role.USER, new ArrayList<>());
    }

    @DisplayName("Member가 작성한 Article 개수를 Emotion 별로 받아온다")
    @Test
    void findArticleEmotionsCountTest() {
        List<Integer> expected = Arrays.asList(2, 1, 1, 0, 3, 0);
        when(emotionService.getAllEmotionsIds()).thenReturn(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L));
        when(articleEmotionService.findArticleEmotionsCount(any(), any())).thenReturn(expected);

        List<Integer> result = analysisService.findArticleEmotionsCount(member);

        assertThat(result.get(0)).isEqualTo(expected.get(0));
        assertThat(result.get(1)).isEqualTo(expected.get(1));
        assertThat(result.get(2)).isEqualTo(expected.get(2));
        assertThat(result.get(3)).isEqualTo(expected.get(3));
        assertThat(result.get(4)).isEqualTo(expected.get(4));
        assertThat(result.get(5)).isEqualTo(expected.get(5));
    }

    @DisplayName("Member가 작성한 Article 중 가장 많은 감정 대분류 ID를 받아온다")
    @Test
    void findMostEmotionIdTest() {
        Long expected = 1L;
        when(articleEmotionService.findMostEmotionIdInArticles(any())).thenReturn(expected);

        Long result = analysisService.findMostEmotionId(member);

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Member가 작성한 Article이 없으면 NOT_EXIST_MOST_EMOTION_ID를 받아온다")
    @Test
    void findNotExistMostEmotionIdTest() {
        Long expected = NOT_EXIST_MOST_EMOTION_ID;

        Member tempMember = new Member();
        when(articleEmotionService.findMostEmotionIdInArticles(any())).thenReturn(expected);
        Long result = analysisService.findMostEmotionId(tempMember);

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Member가 작성한 Comment의 총 개수를 받아온다")
    @Test
    void countTotalCommentsByTest() {
        Long expected = 1L;
        when(commentService.countTotalCommentsBy(any(Member.class))).thenReturn(expected);

        Long result = analysisService.countTotalCommentsBy(member);

        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("Member가 작성한 Comment들의 CommentLike 총 개수를 받아온다")
    @Test
    void countLikedCommentsByTest() {
        Long expected = 4L;

        List<CommentLike> likes = new ArrayList<>(Arrays.asList(new CommentLike(), new CommentLike()));
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(1L, "content1", new Member(), "nickname1", LocalDateTime.now(), new Article(), false,
                                 likes));
        comments.add(new Comment(2L, "content2", new Member(), "nickname2", LocalDateTime.now(), new Article(), false,
                                 likes));

        when(commentService.findAllCommentsBy(any(Member.class))).thenReturn(comments);

        Long result = analysisService.countTotalCommentLikesBy(member);

        assertThat(result).isEqualTo(expected);
    }
}
