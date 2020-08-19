package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class AnalysisController {
    private final MemberRepository memberRepository;
    private final AnalysisService analysisService;

    @GetMapping("/analysis/articles")
    public ResponseEntity<ArticlesAnalysisResponse> getArticlesAnalysis(Authentication authentication) {
        // TODO: 2020/08/16 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        List<Integer> articleEmotionsCount = analysisService.findArticleEmotionsCount(member);
        Long mostEmotionId = analysisService.findMostEmotionId(member);

        ArticlesAnalysisResponse articlesAnalysisResponse = new ArticlesAnalysisResponse(articleEmotionsCount,
                                                                                         mostEmotionId);

        return ResponseEntity.ok(articlesAnalysisResponse);
    }

    @GetMapping("/analysis/comments")
    public ResponseEntity<CommentsAnalysisResponse> getCommentsAnalysis(Authentication authentication) {
        // TODO: 2020/08/16 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        Long totalCommentsCount = analysisService.countTotalCommentsBy(member);

        // TODO: 2020/08/17 차후 추천받은 댓글의 수와, 그에 따른 메세지도 같이 전달?
        CommentsAnalysisResponse commentsAnalysisResponse = new CommentsAnalysisResponse(totalCommentsCount);

        return ResponseEntity.ok(commentsAnalysisResponse);
    }
}
