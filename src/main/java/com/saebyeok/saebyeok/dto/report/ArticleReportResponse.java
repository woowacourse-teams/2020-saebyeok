package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.report.ArticleReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReportResponse {
    private Long id;
    private String content;
    private Long articleId;
    private ReportCategoryResponse reportCategory;
    private Boolean isFinished;

    public ArticleReportResponse(ArticleReport articleReport) {
        this.id = articleReport.getId();
        this.content = articleReport.getContent();
        this.articleId = articleReport.getArticle().getId();
        this.reportCategory = new ReportCategoryResponse(articleReport.getReportCategory());
        this.isFinished = articleReport.getIsFinished();
    }

    public boolean getIsFinished() {
        return isFinished;
    }
}
