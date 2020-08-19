package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final MemberRepository memberRepository;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> getArticles(Authentication authentication, @RequestParam int page, @RequestParam int size, @RequestParam(required = false) List<Long> emotionIds) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        List<ArticleResponse> articles = articleService.getArticles(member, page, size, emotionIds);
        return ResponseEntity.ok(articles);
    }

    @PostMapping("/articles")
    public ResponseEntity<Void> createArticle(Authentication authentication, @RequestBody ArticleCreateRequest request) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        Article article = articleService.createArticle(member, request);
        return ResponseEntity
                .created(URI.create("/articles/" + article.getId()))
                .build();
    }

    @GetMapping("/articles/{articleId}")
    public ResponseEntity<ArticleResponse> readArticle(Authentication authentication, @PathVariable Long articleId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        ArticleResponse articleResponse = articleService.readArticle(member, articleId);
        return ResponseEntity.ok(articleResponse);
    }

    @DeleteMapping("/articles/{articleId}")
    public ResponseEntity<Void> deleteArticle(Authentication authentication, @PathVariable Long articleId) throws IllegalAccessException {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        articleService.deleteArticle(member, articleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/member/articles")
    public ResponseEntity<List<ArticleResponse>> getMemberArticles(Authentication authentication, @RequestParam int page, @RequestParam int size, @RequestParam(required = false) List<Long> emotionIds) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        List<ArticleResponse> articles = articleService.getMemberArticles(member, page, size, emotionIds);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/member/articles/{articleId}")
    public ResponseEntity<ArticleResponse> readMemberArticle(Authentication authentication, @PathVariable Long articleId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        ArticleResponse articleResponse = articleService.readMemberArticle(member, articleId);
        return ResponseEntity.ok(articleResponse);
    }
}
