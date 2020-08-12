package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private Boolean isDeleted;
    private LocalDateTime createdDate;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getNickname();
        this.isDeleted = comment.getIsDeleted();
        this.createdDate = comment.getCreatedDate();
    }
}
