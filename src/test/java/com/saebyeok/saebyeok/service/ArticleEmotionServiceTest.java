package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleEmotionNotFoundException;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.*;

import static com.saebyeok.saebyeok.service.ArticleEmotionService.NOT_EXIST_MOST_EMOTION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
class ArticleEmotionServiceTest {
    private static final Long ARTICLE_1_ID = 1L;
    private static final Long ARTICLE_2_ID = 2L;
    private static final Long ARTICLE_3_ID = 3L;
    private static final Long EMOTION_1_ID = 1L;
    private static final Long EMOTION_2_ID = 2L;
    private static final Long EMOTION_3_ID = 3L;

    private ArticleEmotionService articleEmotionService;

    @Mock
    private EmotionRepository emotionRepository;

    @Mock
    private ArticleEmotionRepository articleEmotionRepository;

    private Article article1;
    private Article article2;
    private Article article3;
    private Emotion emotion1;
    private Emotion emotion2;
    private ArticleEmotion articleEmotion1;
    private ArticleEmotion articleEmotion2;
    private ArticleEmotion articleEmotion3;

    @BeforeEach
    void setUp() {
        this.articleEmotionService = new ArticleEmotionService(emotionRepository, articleEmotionRepository);

        this.article1 = new Article(ARTICLE_1_ID, "내용1", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        this.article2 = new Article(ARTICLE_2_ID, "내용2", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        this.article3 = new Article(ARTICLE_3_ID, "내용3", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        this.emotion1 = new Emotion(EMOTION_1_ID, "기뻐요", "imageResourceUri1");
        this.emotion2 = new Emotion(EMOTION_2_ID, "슬퍼요", "imageResourceUri2");
        this.articleEmotion1 = new ArticleEmotion(article1, emotion1);
        this.articleEmotion2 = new ArticleEmotion(article2, emotion1);
        this.articleEmotion3 = new ArticleEmotion(article3, emotion2);
    }

    @DisplayName("새로 생성한 게시글과 이에 속하는 감정 대분류의 Id를 통해 ArticleEmotion을 생성한다")
    @Test
    void createArticleEmotionTest() {
        when(emotionRepository.findById(EMOTION_1_ID)).thenReturn(Optional.of(emotion1));

        articleEmotionService.createArticleEmotion(article1, EMOTION_1_ID);

        verify(articleEmotionRepository).save(any());
    }

    @DisplayName("예외 테스트: 새로 생성한 게시글에 속하는 감정 대분류의 Id가 존재하지 않으면 EmotionNotFoundException이 발생한다")
    @Test
    void createArticleEmotionExceptionTest() {
        long invalidEmotionId = -1L;

        when(emotionRepository.findById(invalidEmotionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleEmotionService.createArticleEmotion(article1, invalidEmotionId))
                .isInstanceOf(EmotionNotFoundException.class)
                .hasMessage(invalidEmotionId + "에 해당하는 감정 대분류를 찾을 수 없습니다.");
    }

    @DisplayName("생성된 게시글로 만들어진 ArticleEmotion을 통해 해당 게시글의 감정 대분류 정보를 가져온다")
    @Test
    void findEmotionTest() {
        ArticleEmotion articleEmotion = new ArticleEmotion(article1, emotion1);

        when(articleEmotionRepository.findByArticleId(ARTICLE_1_ID)).thenReturn(Optional.of(articleEmotion));

        EmotionResponse emotionResponse = articleEmotionService.findEmotion(article1);

        assertThat(emotionResponse).isNotNull();
        assertThat(emotionResponse.getName()).isEqualTo(emotion1.getName());
        assertThat(emotionResponse.getImageResource()).isEqualTo(emotion1.getImageResource());
    }

    @DisplayName("예외 테스트: 생성된 게시글로 만들어진 ArticleEmotion이 존재하지 않으면 해당 게시글의 감정 대분류 정보를 가져올 때 " +
            "ArticleEmotionNotFoundException이 발생한다")
    @Test
    void findEmotionExceptionTest() {
        when(articleEmotionRepository.findByArticleId(ARTICLE_1_ID)).thenThrow(new ArticleEmotionNotFoundException(ARTICLE_1_ID));

        assertThatThrownBy(() -> articleEmotionService.findEmotion(article1))
                .isInstanceOf(ArticleEmotionNotFoundException.class)
                .hasMessage(ARTICLE_1_ID + "에 해당하는 게시글의 감정 대분류를 찾을 수 없습니다.");
    }

    @DisplayName("특정 감정 대분류에 속하는 게시글 목록을 요청받은 페이지 수만큼 가져온다")
    @Test
    void findArticlesByEmotionIdsTest() {
        List<Article> articles = Arrays.asList(article1, article2, article3);
        List<Long> emotionIds = Arrays.asList(EMOTION_1_ID, EMOTION_2_ID);
        List<Emotion> emotions = Arrays.asList(emotion1, emotion2);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<ArticleEmotion> articleEmotions = Arrays.asList(articleEmotion1, articleEmotion2, articleEmotion3);

        when(emotionRepository.findAllById(emotionIds)).thenReturn(emotions);
        when(articleEmotionRepository.findAllByArticleInAndEmotionIn(articles, emotions, pageable)).thenReturn(articleEmotions);

        List<Article> articlesByEmotionIds = articleEmotionService.findArticlesByEmotionIds(articles, emotionIds,
                                                                                            pageable);

        assertThat(articlesByEmotionIds).hasSize(articles.size());
    }

    @DisplayName("각 감정 대분류마다 특정 멤버의 게시글이 몇 개가 존재하는지 ArticleEmotion을 통해 구한다")
    @Test
    void findArticleEmotionsCountTest() {
        List<Article> memberArticles = Arrays.asList(article1, article2, article3);
        List<Long> allEmotionsIds = Arrays.asList(EMOTION_1_ID, EMOTION_2_ID, EMOTION_3_ID);
        List<ArticleEmotion> articleEmotions = Arrays.asList(articleEmotion1, articleEmotion2, articleEmotion3);

        when(articleEmotionRepository.findAllByArticleIn(memberArticles)).thenReturn(articleEmotions);

        List<Integer> articleEmotionsCount = articleEmotionService.findArticleEmotionsCount(memberArticles,
                                                                                            allEmotionsIds);

        assertThat(articleEmotionsCount).hasSize(allEmotionsIds.size())
                .isEqualTo(Arrays.asList(2, 1, 0));
    }

    @DisplayName("특정 멤버의 게시글에서 가장 많은 감정 대분류의 id가 무엇인지 ArticleEmotion을 통해 구한다")
    @Test
    void findMostEmotionIdInArticlesTest() {
        List<Article> memberArticles = Arrays.asList(article1, article2, article3);
        List<ArticleEmotion> memberArticleEmotions = Arrays.asList(articleEmotion1, articleEmotion2, articleEmotion3);

        when(articleEmotionRepository.findAllByArticleIn(memberArticles)).thenReturn(memberArticleEmotions);

        Long mostEmotionIdInArticles = articleEmotionService.findMostEmotionIdInArticles(memberArticles);

        assertThat(mostEmotionIdInArticles).isEqualTo(EMOTION_1_ID);
    }

    @DisplayName("특정 멤버가 작성한 게시글이 없으면 NOT_EXIST_MOST_EMOTION_ID을 반환한다")
    @Test
    void findNotExistMostEmotionIdInArticlesTest() {
        List<Article> memberArticles = Collections.emptyList();

        Long mostEmotionIdInArticles = articleEmotionService.findMostEmotionIdInArticles(memberArticles);

        assertThat(mostEmotionIdInArticles).isEqualTo(NOT_EXIST_MOST_EMOTION_ID);
    }
}
