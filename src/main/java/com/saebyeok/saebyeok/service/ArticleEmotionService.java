package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleEmotionNotFoundException;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    public List<Article> findArticlesByEmotionIds(List<Article> articles, List<Long> emotionIds, Pageable pageable) {
        List<Emotion> emotions = emotionRepository.findAllById(emotionIds);
        return articleEmotionRepository.findAllByArticleInAndEmotionIn(articles, emotions, pageable).
                stream().
                map(ArticleEmotion::getArticle).
                collect(Collectors.toList());
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
