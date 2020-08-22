package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
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
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;

    @Column(length = 300)
    private String content;

    @ManyToOne
    private Member member;

    @CreatedDate
    private LocalDateTime createdDate;
    private Boolean isCommentAllowed;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @Formula("select count(*) from ARTICLE_LIKE as AL where AL.ARTICLE_ID = ARTICLE_ID")
    private Long likesCount;

    @OneToMany(mappedBy = "article")
    private List<ArticleLike> likes;

    public Article(String content, Boolean isCommentAllowed) {
        this.content = content;
        this.isCommentAllowed = isCommentAllowed;
    }

    public void setMember(Member member) {
        // Todo: 편의 메소드 리팩토링(기존에 member가 있는 경우, add를 중복으로 하는 경우 등)
        this.member = member;
        member.addArticle(this);
    }

    public boolean isWrittenBy(Member member) {
        return this.member == member;
    }

    public boolean isLikedBy(Member member) {
        Objects.requireNonNull(member);
        return this.likes.stream().anyMatch(it -> it.getMember() == member);
    }

    public void addLike(ArticleLike like) {
        Objects.requireNonNull(like);

        if (like.getArticle() != this) {
            // TODO: 2020/08/22 : 커스텀 Exception 생성해서 사용하기
            throw new RuntimeException();
        }

        if (this.likes.contains(like)) {
            throw new DuplicateArticleLikeException(like.getMember().getId(), like.getArticle().getId());
        }
        this.likes.add(like);
    }
}
