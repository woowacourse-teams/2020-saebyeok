package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "ARTICLE_ID")
    @ManyToOne
    private Article article;

    @CreatedDate
    private LocalDateTime createdDate;

    public ArticleLike(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
