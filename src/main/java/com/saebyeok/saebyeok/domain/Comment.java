package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.DuplicateCommentLikeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140, nullable = false)
    private String content;

    @ManyToOne
    private Member member;
    private String nickname;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private Article article;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> likes;

    @Builder
    public Comment(String content, String nickname, Boolean isDeleted) {
        this.content = content;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
    }

    public boolean isWrittenBy(Member member) {
        return this.member == member;
    }

    public boolean isLikedBy(Member member) {
        Objects.requireNonNull(member);
        return this.likes.stream().anyMatch(it -> it.getMember() == member);
    }

    public long countLikes() {
        return this.likes.size();
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addLike(CommentLike like) {
        Objects.requireNonNull(like);

        if (like.getComment() != this) {
            // TODO: 2020/08/22 : 커스텀 Exception 생성해서 사용하기
            throw new RuntimeException();
        }

        if (this.likes.contains(like)) {
            throw new DuplicateCommentLikeException(like.getMember().getId(), like.getComment().getId());
        }
        this.likes.add(like);
    }
}
