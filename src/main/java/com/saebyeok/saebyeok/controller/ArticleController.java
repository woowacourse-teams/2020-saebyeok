package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(@LoginMember Member member,
                                                             @RequestParam int page, @RequestParam int size,
                                                             @RequestParam(required = false) List<Long> emotionIds) {
        List<ArticleResponse> articles = articleService.getArticles(member, page, size, emotionIds);
        return ResponseEntity.ok(articles);
    }

    @PostMapping("/articles")
    public ResponseEntity<Void> createArticle(@LoginMember Member member, @Valid @RequestBody ArticleCreateRequest request) {
        Article article = articleService.createArticle(member, request);
        return ResponseEntity
                .created(URI.create("/articles/" + article.getId()))
                .build();
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> readArticle(@LoginMember Member member, @PathVariable Long articleId) {
        ArticleResponse articleResponse = articleService.readArticle(member, articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(@LoginMember Member member, @PathVariable Long articleId) throws IllegalAccessException {
        articleService.deleteArticle(member, articleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member/articles")
    public ResponseEntity<List<ArticleResponse>> getMemberArticles(@LoginMember Member member,
                                                                   @RequestParam int page, @RequestParam int size,
                                                                   @RequestParam(required = false) List<Long> emotionIds) {
        List<ArticleResponse> articles = articleService.getMemberArticles(member, page, size, emotionIds);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/member/articles/{articleId}")
    public ResponseEntity<ArticleResponse> readMemberArticle(@LoginMember Member member, @PathVariable Long articleId) {
        ArticleResponse articleResponse = articleService.readMemberArticle(member, articleId);
        return ResponseEntity.ok(articleResponse);
    }
}
