package com.saebyeok.saebyeok.domain.report;

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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REPORT_ID")
    private Long id;

    private String content;
    private Long targetContentId;

    @Enumerated(EnumType.STRING)
    private ReportTarget reportTarget;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    @ManyToOne
    private Member member;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "REPORT_CATEGORY_ID", nullable = false)
    @ManyToOne
    private ReportCategory reportCategory;

    @CreatedDate
    private LocalDateTime createdDate;

    private Boolean isFinished = Boolean.FALSE;

    public Report(String content, Member member, ReportTarget reportTarget, Long targetContentId, ReportCategory reportCategory) {
        this.content = content;
        this.member = member;
        this.reportTarget = reportTarget;
        this.targetContentId = targetContentId;
        this.reportCategory = reportCategory;
    }
}
