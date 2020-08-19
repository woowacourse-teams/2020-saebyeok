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
public class CommentLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "MEMBER_ID")
    @ManyToOne
    private Member member;

    @JoinColumn(name = "COMMENT_ID")
    @ManyToOne
    private Comment comment;

    @CreatedDate
    private LocalDateTime createdDate;

    public CommentLike(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }
}
