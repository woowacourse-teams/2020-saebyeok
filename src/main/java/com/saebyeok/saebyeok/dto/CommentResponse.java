package com.saebyeok.saebyeok.dto;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String content;
    private String nickname;
    private Boolean isDeleted;
    private LocalDateTime createdDate;
    private Boolean isMine;
    private Long likesCount;
    private Boolean isLikedByMe;
    private List<RecommentResponse> recomments;

    public CommentResponse(Comment comment, Member member) {
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
