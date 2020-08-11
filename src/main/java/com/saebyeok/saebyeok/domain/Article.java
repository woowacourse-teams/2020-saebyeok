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
import java.util.stream.Collectors;

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
    private String emotion;
    private Boolean isCommentAllowed;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @OneToMany(mappedBy = "article")
    private List<ArticleSubEmotion> articleSubEmotions;

    public Article(String content, String emotion, Boolean isCommentAllowed) {
        this.content = content;
        this.emotion = emotion;
        this.isCommentAllowed = isCommentAllowed;
    }

    public void setMember(Member member) {
        // Todo: 편의 메소드 리팩토링(기존에 member가 있는 경우, add를 중복으로 하는 경우 등)
        this.member = member;
        member.addArticle(this);
    }

    public void setArticleSubEmotions(List<SubEmotion> subEmotions) {
        if (Objects.isNull(subEmotions) || subEmotions.isEmpty()) {
            this.articleSubEmotions = new ArrayList<>();
            return;
        }
        this.articleSubEmotions = subEmotions.
                stream().
                map(subEmotion -> new ArticleSubEmotion(this, subEmotion)).
                collect(Collectors.toList());
    }
}
