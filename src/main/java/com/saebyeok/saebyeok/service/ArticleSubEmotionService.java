package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleSubEmotion;
import com.saebyeok.saebyeok.domain.ArticleSubEmotionRepository;
import com.saebyeok.saebyeok.domain.SubEmotionRepository;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleSubEmotionNotFoundException;
import com.saebyeok.saebyeok.exception.SubEmotionNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleSubEmotionService {
    private final SubEmotionRepository subEmotionRepository;
    private final ArticleSubEmotionRepository articleSubEmotionRepository;

    public void createArticleSubEmotion(Article article, List<Long> subEmotionIds) {
        List<ArticleSubEmotion> articleSubEmotions = subEmotionIds.
                stream().
                map(subEmotionId -> subEmotionRepository.findById(subEmotionId).
                        orElseThrow(() -> new SubEmotionNotFoundException(subEmotionId))).
                map(subEmotion -> new ArticleSubEmotion(article, subEmotion)).
                collect(Collectors.toList());

        for (ArticleSubEmotion articleSubEmotion : articleSubEmotions) {
            articleSubEmotionRepository.save(articleSubEmotion);
        }
    }

    public List<SubEmotionResponse> findSubEmotionResponses(Article article) {
        List<ArticleSubEmotion> articleSubEmotions = articleSubEmotionRepository.findAllByArticleId(article.getId());

        return articleSubEmotions.
                stream().
                map(ArticleSubEmotion::getSubEmotion).
                map(SubEmotionResponse::new).
                collect(Collectors.toList());
    }

    public void deleteArticleSubEmotion(Article article) {
        try {
            articleSubEmotionRepository.deleteAllByArticleId(article.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new ArticleSubEmotionNotFoundException(article.getId());
        }
    }

}
