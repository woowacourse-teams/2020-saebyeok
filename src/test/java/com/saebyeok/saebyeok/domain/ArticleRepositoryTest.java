package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
class ArticleRepositoryTest {
    private static final LocalDateTime LIMIT_DATE = LocalDateTime.now().minusDays(7);
    private static final PageRequest PAGE_REQUEST = PageRequest.of(0, 10);

    @Autowired
    private ArticleRepository articleRepository;

    private Article article1;
    private Article article2;
    private Article article3;

    @BeforeEach
    void setUp() {
        article1 = new Article("내용1", "기뻐요", true);
        article2 = new Article("내용2", "슬퍼요", false);
        article3 = new Article("내용3", "화나요", true);

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);
    }

    @DisplayName("게시글 전체 조회를 한다")
    @Test
    void findAllByCreatedDateTest() {
        List<Article> articles = articleRepository.findAllByCreatedDateGreaterThanEqual(LIMIT_DATE, PAGE_REQUEST);

        assertThat(articles).
                hasSize(3).
                extracting("content").
                containsOnly(article1.getContent(), article2.getContent(), article3.getContent());
        assertThat(articles).
                extracting("emotion").
                containsOnly(article1.getEmotion(), article2.getEmotion(), article3.getEmotion());
        assertThat(articles).
                extracting("isCommentAllowed").
                containsOnly(article1.getIsCommentAllowed(), article2.getIsCommentAllowed(), article3.getIsCommentAllowed());
    }

    @DisplayName("게시글을 저장한다")
    @Test
    void saveTest() {
        Article newArticle = new Article("내용4", "행복해요", true);
        int articleSize = articleRepository.findAll().size();

        articleRepository.save(newArticle);

        List<Article> articlesAfterSave = articleRepository.findAll();
        assertThat(articlesAfterSave).hasSize(articleSize + 1).contains(newArticle);
    }

    @DisplayName("ID로 게시글을 조회하면 해당 게시글이 반환된다")
    @Test
    void findByIdAndCreatedDateTest() {
        Article savedArticle = articleRepository.findByIdAndCreatedDateGreaterThanEqual(article1.getId(), LIMIT_DATE)
                .orElseThrow(() -> new ArticleNotFoundException(article1.getId()));

        assertThat(savedArticle).isEqualTo(article1);
    }

    @DisplayName("ID로 게시글을 삭제요청하면 해당 게시글이 삭제된다")
    @Test
    void deleteByIdTest() {
        int articleSize = articleRepository.findAll().size();

        articleRepository.deleteById(article1.getId());

        List<Article> articlesAfterDelete = articleRepository.findAll();
        assertThat(articlesAfterDelete).hasSize(articleSize - 1).doesNotContain(article1);
    }
}