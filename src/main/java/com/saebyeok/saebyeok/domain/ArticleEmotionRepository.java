package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleEmotionRepository extends JpaRepository<ArticleEmotion, Long> {
    public Optional<ArticleEmotion> findByArticleId(Long articleId);

    public List<ArticleEmotion> findAllByEmotionId(Long emotionId);

    public void deleteByArticleId(Long articleId);

    @Query("select AE.emotion.id as emotionId, count(AE) as articleCount from ArticleEmotion AE " +
            "where AE.emotion.id in :emotionIds and AE.article.id in :articleIds group by AE.emotion.id")
    public List<ArticleEmotionCount> countArticlesByEmotionIds(@Param("articleIds") List<Long> articleIds,
                                                               @Param("emotionIds") List<Long> emotionIds);
}
