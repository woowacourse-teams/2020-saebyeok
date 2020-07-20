package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequest {
    // TODO: 2020/07/20 member, article은 id로 주입받게 수정 필요
    private String content;
    private Member member;
    private String nickname;
    private Article article;
    private Boolean isDeleted;
}
