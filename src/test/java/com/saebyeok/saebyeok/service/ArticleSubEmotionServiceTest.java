package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.SubEmotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleSubEmotionServiceTest {
    private static final Long ARTICLE_ID = 1L;
    private static final Long SUB_EMOTION_1_ID = 1L;
    private static final Long SUB_EMOTION_2_ID = 2L;
    private static final Long SUB_EMOTION_3_ID = 3L;

    private ArticleSubEmotionService articleSubEmotionService;

    @Mock
    private SubEmotionRepository subEmotionRepository;

    @Mock
    private ArticleSubEmotionRepository articleSubEmotionRepository;

    private Article article;
    private SubEmotion subEmotion1;
    private SubEmotion subEmotion2;
    private SubEmotion subEmotion3;

    @BeforeEach
    void setUp() {
        this.articleSubEmotionService = new ArticleSubEmotionService(subEmotionRepository, articleSubEmotionRepository);

        this.article = new Article(ARTICLE_ID, "내용", null, LocalDateTime.now(), true, false, new ArrayList<>());
        this.subEmotion1 = new SubEmotion(SUB_EMOTION_1_ID, "행복해요");
        this.subEmotion2 = new SubEmotion(SUB_EMOTION_2_ID, "신나요");
        this.subEmotion3 = new SubEmotion(SUB_EMOTION_3_ID, "멋져요");
    }

    @DisplayName("새로 생성한 게시글과 이에 속하는 감정 소분류의 Id를 통해 ArticleSubEmotion을 생성한다")
    @Test
    void createArticleSubEmotionTest() {
        List<Long> subEmotionIds = Arrays.asList(SUB_EMOTION_1_ID, SUB_EMOTION_2_ID, SUB_EMOTION_3_ID);

        when(subEmotionRepository.findById(SUB_EMOTION_1_ID)).thenReturn(Optional.of(subEmotion1));
        when(subEmotionRepository.findById(SUB_EMOTION_2_ID)).thenReturn(Optional.of(subEmotion2));
        when(subEmotionRepository.findById(SUB_EMOTION_3_ID)).thenReturn(Optional.of(subEmotion3));

        articleSubEmotionService.createArticleSubEmotion(article, subEmotionIds);

        verify(articleSubEmotionRepository).saveAll(anyList());
    }

    @DisplayName("예외 테스트: 새로 생성한 게시글에 속하는 감정 소분류의 Id가 존재하지 않으면 SubEmotionNotFoundException이 발생한다")
    @Test
    void createArticleSubEmotionExceptionTest() {
        long invalidSubEmotionId = -1L;
        List<Long> subEmotionIds = Arrays.asList(invalidSubEmotionId, SUB_EMOTION_1_ID);

        when(subEmotionRepository.findById(invalidSubEmotionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articleSubEmotionService.createArticleSubEmotion(article, subEmotionIds))
                .isInstanceOf(SubEmotionNotFoundException.class)
                .hasMessage(invalidSubEmotionId + "에 해당하는 감정 소분류를 찾을 수 없습니다.");
    }

    @DisplayName("새로 생성한 게시글에 속하는 감정 소분류가 없으면 ArticleSubEmotion을 생성하지 않는다")
    @Test
    void tt() {
        articleSubEmotionService.createArticleSubEmotion(article, Collections.emptyList());

        verify(articleSubEmotionRepository, never()).saveAll(anyList());
    }

    @DisplayName("생성된 게시글로 만들어진 ArticleSubEmotion을 통해 해당 게시글의 감정 소분류 정보를 가져온다")
    @Test
    void findSubEmotionsTest() {
        ArticleSubEmotion articleSubEmotion1 = new ArticleSubEmotion(article, subEmotion1);
        ArticleSubEmotion articleSubEmotion2 = new ArticleSubEmotion(article, subEmotion2);
        ArticleSubEmotion articleSubEmotion3 = new ArticleSubEmotion(article, subEmotion3);
        List<ArticleSubEmotion> articleSubEmotions = Arrays.asList(articleSubEmotion1, articleSubEmotion2,
                                                                   articleSubEmotion3);

        when(articleSubEmotionRepository.findAllByArticleId(ARTICLE_ID)).thenReturn(articleSubEmotions);

        List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);

        assertAll(
                () -> assertThat(subEmotionResponses)
                        .isNotNull()
                        .hasSize(articleSubEmotions.size()),
                () -> assertThat(subEmotionResponses.get(0).getName()).isEqualTo(subEmotion1.getName()),
                () -> assertThat(subEmotionResponses.get(1).getName()).isEqualTo(subEmotion2.getName()),
                () -> assertThat(subEmotionResponses.get(2).getName()).isEqualTo(subEmotion3.getName())
        );
    }
}
