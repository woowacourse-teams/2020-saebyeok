package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.*;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Article createArticle(Member member, ArticleCreateRequest request) {
        // Todo: 초기 개발을 위한 member 생성 로직 삭제하고 security 적용하면 member 받아오기
        Member test = new Member(1L, 1991, Gender.FEMALE, LocalDateTime.now(), false, new ArrayList<>());
        Article article = request.toArticle();
        article.setMember(test);
        return articleRepository.save(article);
    }

    public ArticleResponse readArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFoundException(articleId));
        return toArticleResponse(article);
    }

    public void deleteArticle(Long articleId) {
        articleRepository.findById(articleId)
            .orElseThrow(() -> new ArticleNotFoundException(articleId));
        articleRepository.deleteById(articleId);
    }

    private ArticleResponse toArticleResponse(Article article) {
        return new ArticleResponse(
            article.getId(),
            article.getContent(),
            article.getCreatedDate(),
            article.getEmotion(),
            article.getIsCommentAllowed(),
            toCommentResponses(article.getComments()));
    }

    private List<CommentResponse> toCommentResponses(List<Comment> comments) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        if (Objects.isNull(comments) || comments.isEmpty()) {
            return commentResponses;
        }
        for (Comment comment : comments) {
            commentResponses.add(new CommentResponse(comment));
        }
        return commentResponses;
    }
}
