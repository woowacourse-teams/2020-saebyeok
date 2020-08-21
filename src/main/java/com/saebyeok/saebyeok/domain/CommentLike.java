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
@Table(uniqueConstraints = @UniqueConstraint(name = "MEMBER_COMMENT_UNIUQE", columnNames = {"MEMBER_ID", "COMMENT_ID"}))
public class CommentLike {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ManyToOne
    private Member member;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    @ManyToOne
    private Comment comment;

    @CreatedDate
    private LocalDateTime createdDate;

    public CommentLike(Member member, Comment comment) {
        Objects.requireNonNull(member);
        Objects.requireNonNull(comment);

        this.member = member;
        this.comment = comment;
    }
}
