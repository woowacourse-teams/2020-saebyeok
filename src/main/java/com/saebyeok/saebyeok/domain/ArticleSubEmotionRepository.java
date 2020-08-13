package com.saebyeok.saebyeok.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleSubEmotionRepository extends JpaRepository<ArticleSubEmotion, Long> {
    public List<ArticleSubEmotion> findAllByArticleId(Long articleId);

    public void deleteAllByArticleId(Long articleId);
}
