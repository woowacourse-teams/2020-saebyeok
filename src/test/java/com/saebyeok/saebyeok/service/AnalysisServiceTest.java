package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.ArticleAnalysisMessage;
import com.saebyeok.saebyeok.domain.ArticleAnalysisMessageRepository;
import com.saebyeok.saebyeok.domain.Emotion;
import com.saebyeok.saebyeok.exception.ArticleAnalysisMessageNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import static com.saebyeok.saebyeok.service.ArticleEmotionService.NOT_EXIST_MOST_EMOTION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Sql("/truncate.sql")
@ExtendWith(MockitoExtension.class)
public class AnalysisServiceTest {
    private AnalysisService analysisService;

    @Mock
    private ArticleAnalysisMessageRepository articleAnalysisMessageRepository;

    @BeforeEach
    void setUp() {
        analysisService = new AnalysisService(articleAnalysisMessageRepository);
    }

    @DisplayName("특정 Emotion ID로 분석 메세지를 받아온다")
    @Test
    void findArticlesAnalysisMessageTest() {
        Long mostArticleEmotionId = 1L;
        Emotion emotion = new Emotion(mostArticleEmotionId, "기뻐요", "이미지 리소스");
        String message = "기쁜 일이 많았네요! 앞으로도 행복한 일이 가득하기를 바랄게요~";
        ArticleAnalysisMessage articleAnalysisMessage = new ArticleAnalysisMessage(emotion, message);
        when(articleAnalysisMessageRepository.findById(any())).thenReturn(java.util.Optional.of(articleAnalysisMessage));

        String resultMessage = analysisService.findArticlesAnalysisMessage(mostArticleEmotionId);

        assertThat(articleAnalysisMessage.getMessage()).isEqualTo(resultMessage);
    }

    @DisplayName("NOT_EXIST_MOST_EMOTION_ID로 게시글 작성 유도 메세지를 받아온다")
    @Test
    void findArticlesInducementMessageTest() {
        String expected = "아직 작성한 글이 없네요~ 새벽에 이야기를 들려주시면 좋겠어요 :)";
        String resultMessage = analysisService.findArticlesAnalysisMessage(NOT_EXIST_MOST_EMOTION_ID);

        assertThat(resultMessage).isEqualTo(expected);
    }

    @DisplayName("예외 테스트: 존재하지 않는 Emotion ID로 분석 메세지를 찾으면 예외가 발생한다")
    @Test
    void findArticlesAnalysisMessageExceptionTest() {
        Long notExistEmotionId = 10L;

        assertThatThrownBy(() -> analysisService.findArticlesAnalysisMessage(notExistEmotionId))
                .isInstanceOf(ArticleAnalysisMessageNotFoundException.class)
                .hasMessage(notExistEmotionId + "에 해당하는 게시글 분석 메시지를 찾을 수 없습니다.");
    }
}
