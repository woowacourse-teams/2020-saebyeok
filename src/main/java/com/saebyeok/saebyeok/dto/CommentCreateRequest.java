package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentCreateRequest {
    private String content;
    private Member member;
    private String nickname;
    private LocalDateTime createdDate;
    private Article article;
    private Boolean isDeleted;
}
