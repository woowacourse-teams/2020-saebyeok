package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.dto.EmotionResponse;
import com.saebyeok.saebyeok.dto.SubEmotionResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    public static final int LIMIT_DAYS = 7;
    private static final String NOT_YOUR_ARTICLE_MESSAGE = "자신의 게시글이 아닙니다!";

    private final ArticleRepository articleRepository;
    private final ArticleEmotionService articleEmotionService;
    private final ArticleSubEmotionService articleSubEmotionService;

    public List<ArticleResponse> getArticles(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return articleRepository.findAllByCreatedDateGreaterThanEqual(LocalDateTime.now().minusDays(LIMIT_DAYS), pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    @Transactional
    public Article createArticle(Member member, ArticleCreateRequest request) {
        Article article = request.toArticle();
        article.setMember(member);

        articleEmotionService.createArticleEmotion(article, request.getEmotionId());
        articleSubEmotionService.createArticleSubEmotion(article, request.getSubEmotionIds());

        return articleRepository.save(article);
    }

    public ArticleResponse readArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqual(articleId,
                                                                                   LocalDateTime.now().minusDays(LIMIT_DAYS))
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
        List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);

        return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
    }

    @Transactional
    public void deleteArticle(Member member, Long articleId) throws IllegalAccessException {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        if (!article.isWrittenBy(member)) {
            throw new IllegalAccessException(NOT_YOUR_ARTICLE_MESSAGE);
        }
        articleEmotionService.deleteArticleEmotion(article);
        articleSubEmotionService.deleteArticleSubEmotion(article);
        articleRepository.deleteById(articleId);
    }

    public List<ArticleResponse> getMemberArticles(Member member, int page, int size, List<Long> emotionIds) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if(!Objects.isNull(emotionIds) && !emotionIds.isEmpty()) {
            return filterMemberArticles(member, pageable, emotionIds);
        }

        return articleRepository.findAllByMember(member, pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    public ArticleResponse readMemberArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndMemberEquals(articleId, member)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
        List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);

        return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
    }

    // TODO: 2020/08/19 얘는 피드용!!
    public List<ArticleResponse> filterArticles(Member member, int page, int size, List<Long> emotionIds) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        List<Article> articles = articleRepository.findAllByCreatedDateGreaterThanEqual(LocalDateTime.now().minusDays(LIMIT_DAYS));

        return articleEmotionService.findArticlesByEmotionIds(articles, emotionIds, pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    private List<ArticleResponse> filterMemberArticles(Member member, Pageable pageable, List<Long> emotionIds) {
        return articleEmotionService.findArticlesByEmotionIds(member.getArticles(), emotionIds, pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }
}
