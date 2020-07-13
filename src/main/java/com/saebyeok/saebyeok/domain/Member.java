package com.saebyeok.saebyeok.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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
    private List<Article> articles;
}
