package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.report.ArticleReport;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReportResponse {
    private Long id;
    private String content;
    private ArticleResponse article;
    private ReportCategoryResponse reportCategory;
    private Boolean isFinished;

    public ArticleReportResponse(ArticleReport articleReport, ArticleResponse articleResponse) {
        this.id = articleReport.getId();
        this.content = articleReport.getContent();
        this.article = articleResponse;
        this.reportCategory = new ReportCategoryResponse(articleReport.getReportCategory());
        this.isFinished = articleReport.getIsFinished();
    }
}
