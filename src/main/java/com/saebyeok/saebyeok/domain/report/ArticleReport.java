package com.saebyeok.saebyeok.domain.report;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @ManyToOne
    private Member member;

    @ManyToOne
    private Article article;

    @ManyToOne
    private ReportCategory reportCategory;

    @CreatedDate
    private LocalDateTime createdDate;

    private boolean isFinished = Boolean.FALSE;

    public void finish() {
        this.isFinished = true;
    }
}
