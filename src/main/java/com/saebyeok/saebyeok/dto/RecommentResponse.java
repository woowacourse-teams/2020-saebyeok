package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RecommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
    private Boolean isMine;
    private Long likesCount;
    private Boolean isLikedByMe;

    public RecommentResponse(Comment comment, Member member) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getNickname();
        this.isDeleted = comment.getIsDeleted();
        this.createdDate = comment.getCreatedDate();
        this.isMine = comment.isWrittenBy(member);
        this.likesCount = comment.countLikes();
        this.isLikedByMe = comment.isLikedBy(member);
    }
}
