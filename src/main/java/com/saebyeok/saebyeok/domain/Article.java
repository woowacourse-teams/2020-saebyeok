package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "article")
    private List<ArticleLike> likes = new ArrayList<>();

    public Article(String content, Member member, Boolean isCommentAllowed) {
        this.content = content;
        this.member = member;
        this.isCommentAllowed = isCommentAllowed;
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

    public void addComment(Comment comment) {
        comment.setArticle(this);
        this.comments.add(comment);
    }
}
