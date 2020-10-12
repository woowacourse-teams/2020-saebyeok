package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.util.NicknameGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.saebyeok.saebyeok.domain.Comment.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    @Size(min = MIN_LENGTH,
            max = MAX_LENGTH,
            message = "댓글은 {min}자 이상 {max}자 이하로 작성할 수 있어요.")
    private String content;

    @NotNull(message = "해당하는 게시글이 없어요.")
    private Long articleId;

    private Long parentId;

    public Comment toComment(Member member, Article article, Comment parent) {
        return builder().
                content(this.content).
                member(member).
                nickname(NicknameGenerator.generate(member, article)).
                article(article).
                parent(parent).
                build();
    }
}
