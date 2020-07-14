package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<ArticleResponse> getArticles() {
        return articleRepository.findAll().stream()
                .map(this::toArticleResponse)
                .collect(Collectors.toList());
    }

    private ArticleResponse toArticleResponse(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getContent(),
                article.getCreatedDate(),
                article.getEmotion(),
                article.getIsCommentAllowed(),
                article.getComments());
    }
}
