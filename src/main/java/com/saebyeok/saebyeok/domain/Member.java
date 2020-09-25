package com.saebyeok.saebyeok.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String oauthId;
    private String loginMethod;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder.Default
    private Boolean isDeleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Article> articles = new ArrayList<>();

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void addArticle(Article article) {
        // Todo: 편의 메소드 리팩토링(add를 중복으로 하는 경우 등)
        this.articles.add(article);
    }

    public void deactivate() {
        this.isDeleted = Boolean.TRUE;
        this.oauthId = "DEACTIVATED";
        this.loginMethod = "DEACTIVATED";
    }
}
