package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
    private String content;
    private Long emotionId;
    private List<Long> subEmotionIds;
    private Boolean isCommentAllowed;

    public Article toArticle() {
        return new Article(content, isCommentAllowed);
    }
}
