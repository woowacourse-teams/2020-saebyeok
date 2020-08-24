package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AnalysisService {
    private static final int INQUIRY_DAYS = 30;
    private static final long NO_COMMENT_LIKED = 0L;

    private final EmotionService emotionService;
    private final ArticleEmotionService articleEmotionService;
    private final CommentService commentService;

    public List<Integer> findArticleEmotionsCount(Member member) {
        List<Article> memberArticles = member.getArticles();
        List<Long> allEmotionsIds = emotionService.getAllEmotionsIds();

        return articleEmotionService.findArticleEmotionsCount(memberArticles, allEmotionsIds);
    }

    public Long findMostEmotionId(Member member) {
        List<Article> articlesByMemberAndInquiryDays = member.getArticles().stream()
                .filter(article -> article.getCreatedDate().isAfter(article.getCreatedDate().minusDays(INQUIRY_DAYS)))
                .collect(Collectors.toList());

        return articleEmotionService.findMostEmotionIdInArticles(articlesByMemberAndInquiryDays);
    }

    public Long countTotalCommentsBy(Member member) {
        return commentService.countTotalCommentsBy(member);
    }

    public Long countLikedCommentsBy(Member member) {
        return commentService.findAllCommentsBy(member).stream()
                .filter(comment -> comment.countLikes() > NO_COMMENT_LIKED)
                .count();
    }
}
