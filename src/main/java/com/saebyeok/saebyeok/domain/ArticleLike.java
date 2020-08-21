package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "MEMBER_ARTICLE_UNIUQE", columnNames = {"MEMBER_ID", "ARTICLE_ID"}))
public class ArticleLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ManyToOne
    private Member member;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    @ManyToOne
    private Article article;

    @CreatedDate
    private LocalDateTime createdDate;

    public ArticleLike(Member member, Article article) {
        Objects.requireNonNull(member);
        Objects.requireNonNull(article);

        this.member = member;
        this.article = article;
    }
}
