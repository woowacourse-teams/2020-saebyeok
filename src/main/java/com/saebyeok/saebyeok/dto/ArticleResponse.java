package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
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
    private List<CommentResponse> comments;

    public ArticleResponse(Article article, EmotionResponse emotion, List<SubEmotionResponse> subEmotions) {
        this.id = article.getId();
        this.content = article.getContent();
        this.createdDate = article.getCreatedDate();
        this.emotion = emotion;
        this.subEmotions = subEmotions;
        this.isCommentAllowed = article.getIsCommentAllowed();
        this.comments = transformComments(article.getComments());
    }

    private List<CommentResponse> transformComments(List<Comment> comments) {
        if (Objects.isNull(comments) || comments.isEmpty()) {
            return new ArrayList<>();
        }
        return comments.
                stream().
                map(CommentResponse::new).
                collect(Collectors.toList());
    }
}
