package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
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
    private String emotion;
    private Boolean isCommentAllowed;
    private List<Comment> comments;
}
