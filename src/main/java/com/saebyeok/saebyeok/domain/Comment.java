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
public class Comment implements Comparable<Comment> {
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 140;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = MAX_LENGTH, nullable = false)
    private String content;

    @ManyToOne
    private Member member;
    private String nickname;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private Article article;
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne
    private Comment parent;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> likes;

    @Builder
    public Comment(String content, Member member, String nickname, Article article, Comment parent) {
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.article = article;
        this.parent = parent;
    }

    public Comment(String content, String nickname, Comment parent) {
        this.content = content;
        this.nickname = nickname;
        this.parent = parent;
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

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    @Override
    public int compareTo(Comment other) {
        Comment parentOfThis = (this.parent == null ? this : this.parent);
        Comment parentOfOther = (other.parent == null ? other : other.parent);
        if (parentOfThis != parentOfOther) {
            return parentOfThis.createdDate.compareTo(parentOfOther.createdDate);
        }
        return this.createdDate.compareTo(other.createdDate);
    }
}