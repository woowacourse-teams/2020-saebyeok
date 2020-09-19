package com.saebyeok.saebyeok.domain.report;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long id;

    private String content;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ManyToOne
    private Member member;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "COMMENT_ID", nullable = false)
    @ManyToOne
    private Comment comment;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "REPORT_CATEGORY_ID", nullable = false)
    @ManyToOne
    private ReportCategory reportCategory;

    @CreatedDate
    private LocalDateTime createdDate;

    private boolean isFinished = Boolean.FALSE;

    public void finish() {
        this.isFinished = true;
    }
}
