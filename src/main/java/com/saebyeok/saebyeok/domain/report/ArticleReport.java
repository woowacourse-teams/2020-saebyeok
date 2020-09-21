package com.saebyeok.saebyeok.domain.report;

import com.saebyeok.saebyeok.domain.Article;
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
public class ArticleReport {

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
    @JoinColumn(name = "ARTICLE_ID", nullable = false)
    @ManyToOne
    private Article article;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "REPORT_CATEGORY_ID", nullable = false)
    @ManyToOne
    private ReportCategory reportCategory;

    @CreatedDate
    private LocalDateTime createdDate;

    private Boolean isFinished = Boolean.FALSE;

    public ArticleReport(String content, Member member, Article article, ReportCategory reportCategory) {
        this.content = content;
        this.member = member;
        this.article = article;
        this.reportCategory = reportCategory;
    }

    public void finish() {
        this.isFinished = true;
    }
}
