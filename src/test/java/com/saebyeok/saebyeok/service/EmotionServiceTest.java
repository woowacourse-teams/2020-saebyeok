package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Emotion;
import com.saebyeok.saebyeok.domain.EmotionRepository;
import com.saebyeok.saebyeok.dto.EmotionDetailResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmotionServiceTest {
    private static final Long EMOTION_ID = 1L;
    private static final String EMOTION_NAME = "기뻐요";
    private static final String EMOTION_IMAGE_RESOURCE = "리소스";
    private static final Long INVALID_EMOTION_ID = 1000L;

    private EmotionService emotionService;

    @Mock
    private EmotionRepository emotionRepository;

    private Emotion emotion;

    @BeforeEach
    void setUp() {
        emotionService = new EmotionService(emotionRepository);
        emotion = new Emotion(EMOTION_ID, EMOTION_NAME, EMOTION_IMAGE_RESOURCE);
    }

    @DisplayName("전체 Emotion을 조회하면 Emotion 목록이 리턴된다")
    @Test
    void getEmotionsTest() {
        List<Emotion> emotions = new ArrayList<>();
        emotions.add(emotion);
        when(emotionRepository.findAll()).thenReturn(emotions);

        List<EmotionResponse> emotionResponses = emotionService.getEmotions();

        assertThat(emotionResponses).hasSize(1);
        assertThat(emotionResponses.get(0).getId()).isEqualTo(EMOTION_ID);
        assertThat(emotionResponses.get(0).getName()).isEqualTo(EMOTION_NAME);
        assertThat(emotionResponses.get(0).getImageResource()).isEqualTo(EMOTION_IMAGE_RESOURCE);
    }

    @DisplayName("ID로 개별 Emotion 조회를 요청하면 해당 Emotion을 전달받는다")
    @Test
    void readEmotionTest() {
        when(emotionRepository.findById(EMOTION_ID)).thenReturn(Optional.of(emotion));

        EmotionDetailResponse emotionDetailResponse = emotionService.readEmotion(EMOTION_ID);

        assertThat(emotionDetailResponse).isNotNull();
        assertThat(emotionDetailResponse.getId()).isEqualTo(EMOTION_ID);
        assertThat(emotionDetailResponse.getName()).isEqualTo(EMOTION_NAME);
        assertThat(emotionDetailResponse.getImageResource()).isEqualTo(EMOTION_IMAGE_RESOURCE);
    }

    @DisplayName("예외 테스트: 요청에 해당하는 ID가 없으면 EmotionNotFoundException이 발생한다")
    @Test
    void readEmotionExceptionTest() {
        when(emotionRepository.findById(INVALID_EMOTION_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> emotionService.readEmotion(INVALID_EMOTION_ID))
                .isInstanceOf(EmotionNotFoundException.class)
                .hasMessage(INVALID_EMOTION_ID + "에 해당하는 감정 대분류를 찾을 수 없습니다.");
    }

    @Test
    void getAllEmotionsIdsTest() {
        List<Emotion> emotions = new ArrayList<>();
        emotions.add(emotion);
        when(emotionRepository.findAll()).thenReturn(emotions);

        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        assertThat(allEmotionsIds).hasSize(1);
        assertThat(allEmotionsIds.get(0)).isEqualTo(emotion.getId());
    }
}
