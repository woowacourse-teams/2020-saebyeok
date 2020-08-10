package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Integer birthYear;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @CreatedDate
    private LocalDateTime createdDate;
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    public String getRoleKey() {
        return this.role.getKey();
    }

    public Member(Long id, Integer birthYear, Gender gender, LocalDateTime createdDate, Boolean isDeleted, List<Article> articles) {
        this.id = id;
        this.birthYear = birthYear;
        this.gender = gender;
        this.createdDate = createdDate;
        this.isDeleted = isDeleted;
        this.articles = articles;
    }

    public void addArticle(Article article) {
        // Todo: 편의 메소드 리팩토링(add를 중복으로 하는 경우 등)
        this.articles.add(article);
    }
}
