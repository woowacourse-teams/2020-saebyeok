package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleEmotionNotFoundException;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleEmotionService {

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

    public void deleteArticleEmotion(Article article) {
        try {
            articleEmotionRepository.deleteByArticleId(article.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new ArticleEmotionNotFoundException(article.getId());
        }
    }

}
