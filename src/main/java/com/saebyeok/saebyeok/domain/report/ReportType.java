package com.saebyeok.saebyeok.domain.report;

import com.saebyeok.saebyeok.exception.ReportTypeNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReportType {
    ARTICLE("ARTICLE"),
    COMMENT("COMMENT");

    private final String name;

    ReportType(String name) {
        this.name = name;
    }

    public static ReportType findReportType(String typeValue) {
        return Arrays.stream(ReportType.values()).
                filter(reportType -> reportType.name().equals(typeValue)).
                findAny().
                orElseThrow(() -> new ReportTypeNotFoundException(typeValue));
    }

    public boolean isArticle() {
        return this.equals(ReportType.ARTICLE);
    }

    public boolean isComment() {
        return this.equals(ReportType.COMMENT);
    }
}
