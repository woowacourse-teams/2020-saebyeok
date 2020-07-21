package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private Boolean isDeleted;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getNickname();
        this.isDeleted = comment.getIsDeleted();
    }
}
