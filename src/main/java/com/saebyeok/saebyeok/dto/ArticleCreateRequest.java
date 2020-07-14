package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
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

    public Article toArticle(Member member) {
        return new Article(content, member, emotion, isCommentAllowed);
    }
}
