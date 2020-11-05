package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticlesAnalysisResponse;
import com.saebyeok.saebyeok.dto.CommentsAnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisService {
    public static final int VISIBLE_DAYS_ON_ANALYSIS = 30;

    private final ArticleService articleService;
    private final EmotionService emotionService;
    private final ArticleEmotionService articleEmotionService;
    private final CommentService commentService;

    public ArticlesAnalysisResponse getArticlesAnalysis(Member member) {
        List<Integer> articleEmotionsCount = findArticleEmotionsCount(member);
        Long mostEmotionId = findMostEmotionId(member);

        return new ArticlesAnalysisResponse(articleEmotionsCount, mostEmotionId);
    }

    public CommentsAnalysisResponse getCommentsAnalysis(Member member) {
        Long totalCommentsCount = commentService.countTotalCommentsBy(member);
        Long totalCommentLikesCount = commentService.findAllCommentsBy(member).stream()
                .mapToLong(Comment::countLikes)
                .sum();

        return new CommentsAnalysisResponse(totalCommentsCount, totalCommentLikesCount);
    }

    private List<Integer> findArticleEmotionsCount(Member member) {
        List<Article> memberArticles = articleService.getVisibleDaysArticles(member, VISIBLE_DAYS_ON_ANALYSIS);
        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        return articleEmotionService.findArticleEmotionsCount(memberArticles, allEmotionsIds);
    }

    private Long findMostEmotionId(Member member) {
        List<Article> articlesByMemberAndVisibleDays = articleService.getVisibleDaysArticles(member, VISIBLE_DAYS_ON_ANALYSIS);

        return articleEmotionService.findMostEmotionIdInArticles(articlesByMemberAndVisibleDays);
    }
}
