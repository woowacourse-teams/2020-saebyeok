package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Gender;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        articleService = new ArticleService(articleRepository);

    }

    @DisplayName("게시글을 조회하면 게시글 목록이 리턴된다.")
    @Test
    void getArticlesTest() {
        List<Article> articles = new ArrayList<>();
        Member member = new Member(1L, 1996, Gender.MALE, LocalDateTime.now(), false, null);
        articles.add(new Article(1L, "내용", member, LocalDateTime.now(), "기뻐요", true, null));
        when(articleRepository.findAll()).thenReturn(articles);

        List<ArticleResponse> articleResponses = articleService.getArticles();

        assertThat(articleResponses).hasSize(1);
        assertThat(articleResponses.get(0).getContent()).isEqualTo("내용");
        assertThat(articleResponses.get(0).getEmotion()).isEqualTo("기뻐요");
        assertThat(articleResponses.get(0).getIsCommentAllowed()).isTrue();
    }
}
