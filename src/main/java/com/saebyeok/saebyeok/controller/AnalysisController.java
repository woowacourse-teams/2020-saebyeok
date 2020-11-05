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

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AnalysisController {
    private final AnalysisService analysisService;

    @GetMapping("/analysis/articles")
    public ResponseEntity<ArticlesAnalysisResponse> getArticlesAnalysis(@LoginMember Member member) {
        ArticlesAnalysisResponse articlesAnalysisResponse = analysisService.getArticlesAnalysis(member);

        return ResponseEntity.ok(articlesAnalysisResponse);
    }

    @GetMapping("/analysis/comments")
    public ResponseEntity<CommentsAnalysisResponse> getCommentsAnalysis(@LoginMember Member member) {
        CommentsAnalysisResponse commentsAnalysisResponse = analysisService.getCommentsAnalysis(member);

        return ResponseEntity.ok(commentsAnalysisResponse);
    }
}
