package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Recomment {
    public static final int MIN_LENGTH = 1;
    public static final int MAX_LENGTH = 140;

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
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne
    private Comment comment;

    @OneToMany(mappedBy = "comment")
    private List<CommentLike> likes;

    @Builder
    public Recomment(String content, Member member, String nickname, Article article, Comment comment) {
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.article = article;
        this.comment = comment;
    }
}
