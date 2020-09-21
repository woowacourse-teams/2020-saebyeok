package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.report.CommentReport;
import com.saebyeok.saebyeok.dto.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportResponse {
    private Long id;
    private String content;
    private CommentResponse comment;
    private ReportCategoryResponse reportCategoryResponse;
    private Boolean isFinished;

    public CommentReportResponse(CommentReport commentReport, CommentResponse commentResponse) {
        this.id = commentReport.getId();
        this.content = commentReport.getContent();
        this.comment = commentResponse;
        this.reportCategoryResponse = new ReportCategoryResponse(commentReport.getReportCategory());
        this.isFinished = commentReport.getIsFinished();
    }
}
