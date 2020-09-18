package com.saebyeok.saebyeok.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleEmotionRepository extends JpaRepository<ArticleEmotion, Long> {
    public Optional<ArticleEmotion> findByArticleId(Long articleId);

    public List<ArticleEmotion> findAllByArticleInAndEmotionIn(List<Article> articles, List<Emotion> emotions, Pageable pageable);

    public List<ArticleEmotion> findAllByArticleIn(List<Article> article);
}
