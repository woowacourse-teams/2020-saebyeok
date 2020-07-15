package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer birthYear;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @CreatedDate
    private LocalDateTime createdDate;
    private Boolean isDeleted;

    @OneToMany(mappedBy = "member")
    private List<Article> articles = new ArrayList<>();

    public void addArticle(Article article) {
        // Todo: 편의 메소드 리팩토링(add를 중복으로 하는 경우 등)
        this.articles.add(article);
    }
}
