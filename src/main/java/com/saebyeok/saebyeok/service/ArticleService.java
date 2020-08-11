package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    public static final int LIMIT_DAYS = 7;
    private static final String NOT_YOUR_ARTICLE = "자신의 게시글이 아닙니다!";

    private final ArticleRepository articleRepository;

    public List<ArticleResponse> getArticles(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return articleRepository.findAllByCreatedDateGreaterThanEqual(LocalDateTime.now().minusDays(LIMIT_DAYS), pageable).
                stream().
                map(article -> new ArticleResponse(article, member)).
                collect(Collectors.toList());
    }

    @Transactional
    public Article createArticle(Member member, ArticleCreateRequest request) {
        Article article = request.toArticle();
        article.setMember(member);
        return articleRepository.save(article);
    }

    public ArticleResponse readArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqual(articleId, LocalDateTime.now().minusDays(LIMIT_DAYS))
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        return new ArticleResponse(article, member);
    }

    @Transactional
    public void deleteArticle(Member member, Long articleId) throws IllegalAccessException {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        if (!article.isWrittenBy(member)) {
            throw new IllegalAccessException(NOT_YOUR_ARTICLE);
        }
        articleRepository.deleteById(articleId);
    }

}
