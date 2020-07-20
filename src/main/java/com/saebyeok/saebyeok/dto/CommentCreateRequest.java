package com.saebyeok.saebyeok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    private String content;
    private Long memberId;
    private String nickname;
    private Long articleId;
    private Boolean isDeleted;
}
