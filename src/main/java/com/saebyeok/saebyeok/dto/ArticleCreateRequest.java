package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
    private String content;
    private String emotion;
    private Boolean isCommentAllowed;

    public Article toArticle() {
        return new Article(content, emotion, isCommentAllowed);
    }
}
