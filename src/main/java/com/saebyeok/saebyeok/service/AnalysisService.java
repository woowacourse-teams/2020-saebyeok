package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.ArticleAnalysisMessage;
import com.saebyeok.saebyeok.domain.ArticleAnalysisMessageRepository;
import com.saebyeok.saebyeok.exception.ArticleAnalysisMessageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.saebyeok.saebyeok.service.ArticleEmotionService.NOT_EXIST_MOST_EMOTION_ID;

@RequiredArgsConstructor
@Service
public class AnalysisService {
    private final ArticleAnalysisMessageRepository articleAnalysisMessageRepository;

    public String getArticlesAnalysisMessage(long mostArticleEmotionId) {
        if (mostArticleEmotionId == NOT_EXIST_MOST_EMOTION_ID) {
            return "아직 작성한 글이 없네요~ 새벽에 이야기를 들려주시면 좋겠어요 :)";
        }

        ArticleAnalysisMessage articleAnalysisMessage =
                articleAnalysisMessageRepository.findById(mostArticleEmotionId)
                        .orElseThrow(() -> new ArticleAnalysisMessageNotFoundException(mostArticleEmotionId));

        return articleAnalysisMessage.getMessage();
    }
}
