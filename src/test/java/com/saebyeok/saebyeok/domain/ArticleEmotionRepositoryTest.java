package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.ArticleEmotionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
class ArticleEmotionRepositoryTest {

    @Autowired
    private ArticleEmotionRepository articleEmotionRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private EmotionRepository emotionRepository;

    private Article article1;
    private Article article2;
    private Article article3;
    private Emotion emotion1;
    private Emotion emotion2;
    private ArticleEmotion articleEmotion1;
    private ArticleEmotion articleEmotion2;
    private ArticleEmotion articleEmotion3;

    @BeforeEach
    void setUp() {
        this.article1 = new Article(1L, "내용1", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        this.article2 = new Article(2L, "내용2", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        this.article3 = new Article(3L, "내용3", null, LocalDateTime.now(), true, false, new ArrayList<>(),
                                    new ArrayList<>());
        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        this.emotion1 = new Emotion(1L, "기뻐요", "imageResourceUri1");
        this.emotion2 = new Emotion(2L, "슬퍼요", "imageResourceUri2");
        emotionRepository.save(emotion1);
        emotionRepository.save(emotion2);

        this.articleEmotion1 = new ArticleEmotion(article1, emotion1);
        this.articleEmotion2 = new ArticleEmotion(article2, emotion1);
        this.articleEmotion3 = new ArticleEmotion(article3, emotion2);
        articleEmotionRepository.save(articleEmotion1);
        articleEmotionRepository.save(articleEmotion2);
        articleEmotionRepository.save(articleEmotion3);
    }

    @DisplayName("기존에 생성된 게시글로 만들어진 ArticleEmotion을 게시글 Id로 찾는다")
    @Test
    void findArticleEmotionByArticleIdTest() {
        ArticleEmotion savedArticleEmotion = articleEmotionRepository.findByArticleId(article1.getId())
                .orElseThrow(() -> new ArticleEmotionNotFoundException(article1.getId()));

        assertThat(savedArticleEmotion).isEqualTo(articleEmotion1);
    }

    @DisplayName("특정 감정 대분류와 게시글에서 만들어진 ArticleEmotion을 요청받은 페이지 수만큼 찾는다")
    @Test
    void findAllArticleEmotionsByArticleInAndEmotionInTest() {
        List<Article> articles = Arrays.asList(article1, article2, article3);
        List<Emotion> emotions = Arrays.asList(emotion1, emotion2);
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "id"));
        List<ArticleEmotion> articleEmotions = Arrays.asList(articleEmotion1, articleEmotion2, articleEmotion3);

        List<ArticleEmotion> actualArticleEmotions = articleEmotionRepository.findAllByArticleInAndEmotionIn(articles,
                                                                                                             emotions,
                                                                                                             pageable);

        assertThat(actualArticleEmotions).hasSize(articleEmotions.size());
    }

    @DisplayName("특정 게시글에서 만들어진 ArticleEmotion을 찾는다")
    @Test
    void findAllArticleEmotionsByArticleInTest() {
        List<Article> articles = Arrays.asList(article1, article2, article3);
        List<ArticleEmotion> articleEmotions = Arrays.asList(articleEmotion1, articleEmotion2, articleEmotion3);

        List<ArticleEmotion> actualArticleEmotions = articleEmotionRepository.findAllByArticleIn(articles);

        assertThat(actualArticleEmotions).hasSize(articleEmotions.size());
    }
}