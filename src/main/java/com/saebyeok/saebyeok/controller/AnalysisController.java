package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import com.saebyeok.saebyeok.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AnalysisController {
    private final AnalysisService analysisService;

    @GetMapping("/analysis/articles")
    public ResponseEntity<ArticlesAnalysisResponse> getArticlesAnalysis(@LoginMember Member member) {
        List<Integer> articleEmotionsCount = analysisService.findArticleEmotionsCount(member);
        Long mostEmotionId = analysisService.findMostEmotionId(member);

        ArticlesAnalysisResponse articlesAnalysisResponse = new ArticlesAnalysisResponse(articleEmotionsCount,
                mostEmotionId);

        return ResponseEntity.ok(articlesAnalysisResponse);
    }

    @GetMapping("/analysis/comments")
    public ResponseEntity<CommentsAnalysisResponse> getCommentsAnalysis(@LoginMember Member member) {
        Long totalCommentsCount = analysisService.countTotalCommentsBy(member);
        Long totalCommentLikesCount = analysisService.countTotalCommentLikesBy(member);

        CommentsAnalysisResponse commentsAnalysisResponse = new CommentsAnalysisResponse(totalCommentsCount,
                totalCommentLikesCount);

        return ResponseEntity.ok(commentsAnalysisResponse);
    }
}
