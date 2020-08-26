package com.saebyeok.saebyeok.domain;

import com.saebyeok.saebyeok.exception.DuplicateArticleLikeException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

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
