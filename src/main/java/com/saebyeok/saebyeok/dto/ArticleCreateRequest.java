package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
    @Size(max = 300, message = "내용은 300자까지 작성 가능합니다.")
    private String content;

    @NotNull(message = "감정 대분류는 반드시 선택되어야 합니다.")
    private Long emotionId;
    private List<Long> subEmotionIds;
    private Boolean isCommentAllowed;

    public Article toArticle() {
        return new Article(content, isCommentAllowed);
    }
}
