package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private List<CommentResponse> comments;

    public ArticleResponse(Article article, Member member, EmotionResponse emotion, List<SubEmotionResponse> subEmotions) {
        this.id = article.getId();
        this.content = article.getContent();
        this.createdDate = article.getCreatedDate();
        this.emotion = emotion;
        this.subEmotions = subEmotions;
        this.isCommentAllowed = article.getIsCommentAllowed();
        this.isMine = article.isWrittenBy(member);
        this.comments = transformComments(article, member);
    }

    private List<CommentResponse> transformComments(Article article, Member member) {
        if (Objects.isNull(article.getComments()) || article.getComments().isEmpty()) {
            return new ArrayList<>();
        }
        return article.getComments().
                stream().
                map(comment -> new CommentResponse(comment, member)).
                collect(Collectors.toList());
    }
}
