package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(Member member) {
        List<ArticleResponse> articles = articleService.getArticles();
        return ResponseEntity.ok(articles);
    }

    @PostMapping("/articles")
    public ResponseEntity<Void> createArticle(Member member, @RequestBody ArticleCreateRequest request) {
        Article article = articleService.createArticle(member, request);

        return ResponseEntity
                .created(URI.create("/articles/" + article.getId()))
                .build();
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> readArticle(Member member, @PathVariable Long articleId) {
        ArticleResponse articleResponse = articleService.readArticle(articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(Member member, @PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<Void> articleNotFoundHandler(ArticleNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
