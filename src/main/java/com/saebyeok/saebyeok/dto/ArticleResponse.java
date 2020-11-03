package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleResponse {
    private Long id;
    private String content;
    private LocalDateTime createdDate;
    private EmotionResponse emotion;
    private List<SubEmotionResponse> subEmotions;
    private Boolean isCommentAllowed;
    private Boolean isMine;
    private Long likesCount;
    private Boolean isLikedByMe;
    private Long commentsSize;

    public ArticleResponse(Article article, Member member, EmotionResponse emotion,
                           List<SubEmotionResponse> subEmotions) {
        this.id = article.getId();
        this.content = article.getContent();
        this.createdDate = article.getCreatedDate();
        this.emotion = emotion;
        this.subEmotions = subEmotions;
        this.isCommentAllowed = article.getIsCommentAllowed();
        this.isMine = article.isWrittenBy(member);
        this.likesCount = article.countLikes();
        this.isLikedByMe = article.isLikedBy(member);
        this.commentsSize = article.countComments();
    }
}
