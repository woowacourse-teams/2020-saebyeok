package com.saebyeok.saebyeok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

import static com.saebyeok.saebyeok.domain.Recomment.MAX_LENGTH;
import static com.saebyeok.saebyeok.domain.Recomment.MIN_LENGTH;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommentCreateRequest {
    @Size(min = MIN_LENGTH,
            max = MAX_LENGTH,
            message = "답글은 {min}자 이상 {max}자 이하로 작성할 수 있어요.")
    private String content;
    private Long articleId;
    private Long commentId;
}
