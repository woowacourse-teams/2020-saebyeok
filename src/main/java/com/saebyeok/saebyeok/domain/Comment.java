package com.saebyeok.saebyeok.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 300)
    private String content;

    @ManyToOne
    private Member member;
    private String nickname;

    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    private Article article;
    private Boolean isDeleted;

    @Builder
    public Comment(String content, Member member, String nickname, Article article, Boolean isDeleted) {
        this.content = content;
        this.member = member;
        this.nickname = nickname;
        this.article = article;
        this.isDeleted = isDeleted;
    }
}
