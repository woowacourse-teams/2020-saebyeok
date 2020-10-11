package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnalysisService {
    private static final int INQUIRY_DAYS = 30;

    private final ArticleService articleService;
    private final EmotionService emotionService;
    private final ArticleEmotionService articleEmotionService;
    private final CommentService commentService;

    public List<Integer> findArticleEmotionsCount(Member member) {
        List<Article> memberArticles = articleService.getLimitedDaysArticles(member, 30);
        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        return articleEmotionService.findArticleEmotionsCount(memberArticles, allEmotionsIds);
    }

    public Long findMostEmotionId(Member member) {
        List<Article> articlesByMemberAndInquiryDays = articleService.getLimitedDaysArticles(member, 30).stream()
                .filter(article -> article.getCreatedDate().isAfter(article.getCreatedDate().minusDays(INQUIRY_DAYS)))
                .collect(Collectors.toList());

        return articleEmotionService.findMostEmotionIdInArticles(articlesByMemberAndInquiryDays);
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
