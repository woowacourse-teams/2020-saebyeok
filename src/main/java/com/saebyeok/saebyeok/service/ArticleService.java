package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.ArticleRepository;
import com.saebyeok.saebyeok.domain.Gender;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    public static final int LIMIT_DAYS = 7;

    private final ArticleRepository articleRepository;
    private final ArticleEmotionService articleEmotionService;
    private final ArticleSubEmotionService articleSubEmotionService;

    public List<ArticleResponse> getArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return articleRepository.findAllByCreatedDateGreaterThanEqual(LocalDateTime.now().minusDays(LIMIT_DAYS), pageable).
                stream().
                map(article -> {
                    EmotionResponse emotionResponse = articleEmotionService.findEmotionResponse(article);
                    List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotionResponses(article);
                    return new ArticleResponse(article, emotionResponse, subEmotionResponses);
                }).
                collect(Collectors.toList());
    }

    @Transactional
    public Article createArticle(Member member, ArticleCreateRequest request) {
        // Todo: 초기 개발을 위한 member 생성 로직 삭제하고 security 적용하면 member 받아오기
        Member test = new Member(1L, 1991, Gender.FEMALE, LocalDateTime.now(), false, new ArrayList<>());
        Article article = request.toArticle();
        article.setMember(test);

        articleEmotionService.createArticleEmotion(article, request.getEmotionId());
        articleSubEmotionService.createArticleSubEmotion(article, request.getSubEmotionIds());

        return articleRepository.save(article);
    }

    public ArticleResponse readArticle(Long articleId) {
        Article article = articleRepository.findByIdAndCreatedDateGreaterThanEqual(articleId, LocalDateTime.now().minusDays(LIMIT_DAYS))
                .orElseThrow(() -> new ArticleNotFoundException(articleId));
        EmotionResponse emotionResponse = articleEmotionService.findEmotionResponse(article);
        List<SubEmotionResponse> subEmotionResponses = articleSubEmotionService.findSubEmotionResponses(article);

        return new ArticleResponse(article, emotionResponse, subEmotionResponses);
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(id));

        articleEmotionService.deleteArticleEmotion(article);
        articleSubEmotionService.deleteArticleSubEmotion(article);
        articleRepository.deleteById(article.getId());
    }
}
