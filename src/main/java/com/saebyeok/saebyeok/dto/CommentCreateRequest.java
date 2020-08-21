package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private String content;
    private Long articleId;
    private Boolean isDeleted;

    public Comment toComment() {
        return Comment.builder().
                content(this.content).
                isDeleted(this.isDeleted).
                build();
    }
}
