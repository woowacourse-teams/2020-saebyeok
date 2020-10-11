package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.report.Report;
import com.saebyeok.saebyeok.domain.report.ReportCategory;
import com.saebyeok.saebyeok.domain.report.ReportTarget;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {
    private String content;

    @NotNull(message = "신고할 게시물을 반드시 선택해 주셔야 해요.")
    private Long reportedId;

    @NotNull(message = "신고 분류는 반드시 선택해 주셔야 해요.")
    private Long reportCategoryId;

    @NotNull(message = "신고할 게시물의 종류는 반드시 선택해 주셔야 해요.")
    private String reportTarget;

    public Report toReport(Member member, ReportCategory reportCategory) {
        return new Report(content, member, ReportTarget.findReportType(this.reportTarget), reportedId, reportCategory);
    }
}
