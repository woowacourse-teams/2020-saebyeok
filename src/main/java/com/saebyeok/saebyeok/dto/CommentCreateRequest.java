package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    @Size(min = 1, max = 140, message = "댓글은 1자 이상 140자 이하로 작성할 수 있어요.")
    private String content;

    @NotNull(message = "해당하는 게시글이 없어요.")
    private Long articleId;
    private Boolean isDeleted;

    public Comment toComment() {
        return Comment.builder().
                content(this.content).
                isDeleted(this.isDeleted).
                build();
    }
}
