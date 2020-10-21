package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.saebyeok.saebyeok.domain.Article.MAX_LENGTH;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCreateRequest {
    @Size(max = MAX_LENGTH, message = "내용은 {max}자까지 작성할 수 있어요.")
    private String content;

    @NotNull(message = "감정 대분류는 반드시 선택되어야 해요.")
    private Long emotionId;
    private List<Long> subEmotionIds = new ArrayList<>();
    private Boolean isCommentAllowed;

    public Article toArticle(Member member) {
        return new Article(content, member, isCommentAllowed);
    }
}
