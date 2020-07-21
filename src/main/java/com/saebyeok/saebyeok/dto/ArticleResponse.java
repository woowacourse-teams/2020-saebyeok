package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
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
    private String emotion;
    private Boolean isCommentAllowed;
    private List<CommentResponse> comments;

    public ArticleResponse(Article article) {
        this.id = article.getId();
        this.content = article.getContent();
        this.createdDate = article.getCreatedDate();
        this.emotion = article.getEmotion();
        this.isCommentAllowed = article.getIsCommentAllowed();
        this.comments = transformComments(article);
    }

    private List<CommentResponse> transformComments(Article article) {
        if (Objects.isNull(article.getComments()) || article.getComments().isEmpty()) {
            return new ArrayList<>();
        }
        return article.getComments().
                stream().
                map(CommentResponse::new).
                collect(Collectors.toList());
    }
}