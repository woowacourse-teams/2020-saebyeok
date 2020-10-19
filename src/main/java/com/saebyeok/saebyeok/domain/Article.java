package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Article {
    public static final int MAX_LENGTH = 300;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;

    @Column(length = MAX_LENGTH)
    private String content;

    @ManyToOne
    private Member member;

    @CreatedDate
    private LocalDateTime createdDate;
    private Boolean isCommentAllowed;
    private Boolean isDeleted = Boolean.FALSE;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

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

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isWrittenBy(Member member) {
        return this.member == member;
    }

    public Optional<String> loadExistingNickname(Member member) {
        return comments.stream()
                .filter(comment -> comment.isWrittenBy(member))
                .findFirst()
                .map(Comment::getNickname);
    }

    public List<String> getAllNicknames() {
        return comments.stream()
                .map(Comment::getNickname)
                .collect(Collectors.toList());
    }

    public boolean isLikedBy(Member member) {
        Objects.requireNonNull(member);
        return this.likes.stream().anyMatch(it -> it.getMember() == member);
    }

    public long countLikes() {
        return this.likes.size();
    }
}
