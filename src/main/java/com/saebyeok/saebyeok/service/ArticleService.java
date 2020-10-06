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

    public static final int FEED_LIMIT_DAYS = 7;
    private static final String NOT_YOUR_ARTICLE_MESSAGE = "자신의 게시글이 아닙니다!";
    public static final int ANALYSIS_LIMIT_DAYS = 30;

    private final ArticleRepository articleRepository;
    private final ArticleEmotionService articleEmotionService;
    private final ArticleSubEmotionService articleSubEmotionService;
    private final CommentService commentService;

    public List<ArticleResponse> getArticles(Member member, int page, int size, List<Long> emotionIds) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if (!Objects.isNull(emotionIds) && !emotionIds.isEmpty()) {
            List<Article> articles = articleRepository.findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime.now().minusDays(FEED_LIMIT_DAYS), false);
            return filterArticles(member, articles, emotionIds, pageable);
        }

        return articleRepository.findAllByCreatedDateGreaterThanEqualAndIsDeleted(LocalDateTime.now().minusDays(FEED_LIMIT_DAYS), false, pageable).
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
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqualAndIsDeleted(articleId,
                                                                                   LocalDateTime.now().minusDays(FEED_LIMIT_DAYS), false)
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
        article.delete();
    }

    public List<ArticleResponse> getMemberArticles(Member member, int page, int size, List<Long> emotionIds) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        if(!Objects.isNull(emotionIds) && !emotionIds.isEmpty()) {
            return filterArticles(member, member.getArticles(), emotionIds, pageable);
        }

        return articleRepository.findAllByMemberAndIsDeleted(member, false, pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    public ArticleResponse readMemberArticle(Member member, Long articleId) {
        Article article = articleRepository.findByIdAndMemberEqualsAndIsDeleted(articleId, member, false)
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
        List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);

        return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
    }

    private List<ArticleResponse> filterArticles(Member member, List<Article> articles, List<Long> emotionIds, Pageable pageable) {
        return articleEmotionService.findArticlesByEmotionIds(articles, emotionIds, pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotion(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotions(article);
                    return new ArticleResponse(article, member, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    public List<Article> getLast1MonthArticles(Member member) {
        return articleRepository.findAllByMemberAndCreatedDateGreaterThanEqualAndIsDeleted(member, LocalDateTime.now().minusDays(ANALYSIS_LIMIT_DAYS), false);
    }
}
