package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.AnalysisService;
import com.saebyeok.saebyeok.service.ArticleEmotionService;
import com.saebyeok.saebyeok.service.ArticleService;
import com.saebyeok.saebyeok.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AnalysisController {
    private static final int INQUIRY_DAYS = 30;

    private final MemberRepository memberRepository;
    private final EmotionService emotionService;
    private final ArticleService articleService;
    private final ArticleEmotionService articleEmotionService;
    private final AnalysisService analysisService;

    @GetMapping("/analysis/articles")
    public ResponseEntity<ArticlesAnalysisResponse> getArticleAnalysis(Authentication authentication) {
        // TODO: 2020/08/16 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        long[] articleEmotionsCount = createArticleEmotionsCount(member, allEmotionsIds);
        String articlesAnalysisMessage = createArticlesAnalysisMessage(member, allEmotionsIds);
        ArticlesAnalysisResponse articlesAnalysisResponse = new ArticlesAnalysisResponse(articleEmotionsCount,
                                                                                         articlesAnalysisMessage);
        return ResponseEntity.ok(articlesAnalysisResponse);
    }

    private long[] createArticleEmotionsCount(Member member, List<Long> allEmotionsIds) {
        List<Long> memberArticlesIds = articleService.getMemberArticlesIds(member);

        return articleEmotionService.getArticleEmotionsCount(memberArticlesIds, allEmotionsIds);
    }

    private String createArticlesAnalysisMessage(Member member, List<Long> allEmotionsIds) {
        LocalDateTime cutDate = LocalDateTime.now().minusDays(INQUIRY_DAYS);

        List<Long> articlesIdsByMemberIdAndCutDate =
                articleService.getMemberArticlesIdsByCutDate(member, cutDate);
        long mostArticleEmotionId = articleEmotionService.getMostEmotionIdInArticles(articlesIdsByMemberIdAndCutDate,
                                                                                     allEmotionsIds);

        return analysisService.getArticlesAnalysisMessage(mostArticleEmotionId);
    }
}
