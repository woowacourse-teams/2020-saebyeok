package com.saebyeok.saebyeok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleCreateRequest {
    private String content;
    private String emotion;
    private Boolean isCommentAllowed;
}
