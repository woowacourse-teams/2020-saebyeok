package com.saebyeok.saebyeok.dto.report;

import com.saebyeok.saebyeok.domain.report.ReportCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportCategoryResponse {
    private Long id;
    private String name;
    private String content;

    public ReportCategoryResponse(ReportCategory reportCategory) {
        this.id = reportCategory.getId();
        this.name = reportCategory.getName();
        this.content = reportCategory.getContent();
    }
}
