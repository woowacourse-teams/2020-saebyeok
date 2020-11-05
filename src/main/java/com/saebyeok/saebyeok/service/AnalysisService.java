package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
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

    public List<Integer> findArticleEmotionsCount(Member member) {
        List<Article> memberArticles = articleService.getVisibleDaysArticles(member, VISIBLE_DAYS_ON_ANALYSIS);
        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        return articleEmotionService.findArticleEmotionsCount(memberArticles, allEmotionsIds);
    }

    public Long findMostEmotionId(Member member) {
        List<Article> articlesByMemberAndVisibleDays = articleService.getVisibleDaysArticles(member, VISIBLE_DAYS_ON_ANALYSIS);

        return articleEmotionService.findMostEmotionIdInArticles(articlesByMemberAndVisibleDays);
    }

    public Long countTotalCommentsBy(Member member) {
        return commentService.countTotalCommentsBy(member);
    }

    public Long countTotalCommentLikesBy(Member member) {
        return commentService.findAllCommentsBy(member).stream()
                .mapToLong(Comment::countLikes)
                .sum();
    }
}
