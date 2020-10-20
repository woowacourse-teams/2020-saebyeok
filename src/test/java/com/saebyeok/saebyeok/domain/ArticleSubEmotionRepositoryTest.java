package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@Sql("/truncate.sql")
@SpringBootTest
class ArticleSubEmotionRepositoryTest {

    @Autowired
    private ArticleSubEmotionRepository articleSubEmotionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private SubEmotionRepository subEmotionRepository;

    private Article article;
    private ArticleSubEmotion articleSubEmotion1;
    private ArticleSubEmotion articleSubEmotion2;
    private ArticleSubEmotion articleSubEmotion3;

    @BeforeEach
    void setUp() {
        this.article = new Article(1L, "내용", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                   new ArrayList<>());
        articleRepository.save(article);

        SubEmotion subEmotion1 = new SubEmotion(1L, "행복해요");
        SubEmotion subEmotion2 = new SubEmotion(2L, "신나요");
        SubEmotion subEmotion3 = new SubEmotion(3L, "멋져요");
        subEmotionRepository.save(subEmotion1);
        subEmotionRepository.save(subEmotion2);
        subEmotionRepository.save(subEmotion3);

        this.articleSubEmotion1 = new ArticleSubEmotion(article, subEmotion1);
        this.articleSubEmotion2 = new ArticleSubEmotion(article, subEmotion2);
        this.articleSubEmotion3 = new ArticleSubEmotion(article, subEmotion3);
        articleSubEmotionRepository.save(articleSubEmotion1);
        articleSubEmotionRepository.save(articleSubEmotion2);
        articleSubEmotionRepository.save(articleSubEmotion3);
    }

    @DisplayName("생성된 게시글로 만들어진 ArticleSubEmotion을 게시글 Id로 찾는다")
    @Test
    void findAllByArticleIdTest() {
        List<ArticleSubEmotion> articleSubEmotions = Arrays.asList(articleSubEmotion1, articleSubEmotion2,
                                                                   articleSubEmotion3);

        List<ArticleSubEmotion> actualArticleSubEmotions =
                articleSubEmotionRepository.findAllByArticleId(article.getId());

        assertThat(actualArticleSubEmotions).hasSize(articleSubEmotions.size());
        assertThat(actualArticleSubEmotions).contains(articleSubEmotion1, articleSubEmotion2, articleSubEmotion3);
    }
}