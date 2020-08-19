package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleEmotionNotFoundException;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleEmotionService {
    public static final Long NOT_EXIST_MOST_EMOTION_ID = 0L;

    private final EmotionRepository emotionRepository;
    private final ArticleEmotionRepository articleEmotionRepository;

    public void createArticleEmotion(Article article, Long emotionId) {
        Emotion emotion = emotionRepository.findById(emotionId).orElseThrow(
                () -> new EmotionNotFoundException(emotionId));
        ArticleEmotion articleEmotion = new ArticleEmotion(article, emotion);

        articleEmotionRepository.save(articleEmotion);
    }

    public EmotionResponse findEmotion(Article article) {
        ArticleEmotion articleEmotion = articleEmotionRepository.findByArticleId(article.getId()).
                orElseThrow(() -> new ArticleEmotionNotFoundException(article.getId()));

        return new EmotionResponse(articleEmotion.getEmotion());
    }

    // TODO: 2020/08/12 추후 필터 사용 시 필요할 메시드 미리 만들어뒀음
    // 프론트에서 선택한 필터 이모지의 아이디를 전달하면, 그 값을 articleService에서 받아서 얘를 다시 호출해주면
    // 7일 제한에 대해서는 아티클서비스에서 관리해주세요
    public List<Article> findArticles(Long emotionId) {
        Emotion emotion = emotionRepository.findById(emotionId).
                orElseThrow(() -> new EmotionNotFoundException(emotionId));
        List<ArticleEmotion> articleEmotions = articleEmotionRepository.findAllByEmotionId(emotion.getId());

        return articleEmotions.
                stream().
                map(ArticleEmotion::getArticle).
                collect(Collectors.toList());
    }

    public void deleteArticleEmotion(Article article) {
        try {
            articleEmotionRepository.deleteByArticleId(article.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new ArticleEmotionNotFoundException(article.getId());
        }
    }

    public List<Integer> findArticleEmotionsCount(List<Article> memberArticles, List<Long> allEmotionsIds) {
        List<ArticleEmotion> memberArticleEmotions = articleEmotionRepository.findAllByArticleIn(memberArticles);

        Map<Long, Integer> articlesCounter = countArticlesPerEmotion(memberArticleEmotions);

        return allEmotionsIds.stream()
                .map(emotionId -> articlesCounter.getOrDefault(emotionId, 0))
                .collect(Collectors.toList());
    }

    public Long findMostEmotionIdInArticles(List<Article> memberArticles) {
        if (memberArticles.isEmpty()) {
            return NOT_EXIST_MOST_EMOTION_ID;
        }

        List<ArticleEmotion> memberArticleEmotions = articleEmotionRepository.findAllByArticleIn(memberArticles);

        Map<Long, Integer> articlesCounter = countArticlesPerEmotion(memberArticleEmotions);

        return articlesCounter.keySet().stream()
                .reduce((id1, id2) -> articlesCounter.get(id1) > articlesCounter.get(id2) ? id1 : id2)
                .orElseThrow(RuntimeException::new);
    }

    private Map<Long, Integer> countArticlesPerEmotion(List<ArticleEmotion> memberArticleEmotions) {
        Map<Long, Integer> articlesCounter = new HashMap<>();

        memberArticleEmotions.stream()
                .map(ArticleEmotion::getEmotion)
                .map(Emotion::getId)
                .forEach(emotionId -> {
                    int articleCount = articlesCounter.getOrDefault(emotionId, 0);
                    articlesCounter.put(emotionId, ++articleCount);
                });

        return articlesCounter;
    }
}
