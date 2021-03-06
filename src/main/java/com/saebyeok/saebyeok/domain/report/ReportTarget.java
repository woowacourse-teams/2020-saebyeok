package com.saebyeok.saebyeok.domain.report;

import com.saebyeok.saebyeok.exception.ReportTargetNotFoundException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReportTarget {
    ARTICLE("ARTICLE"),
    COMMENT("COMMENT");

    private final String name;

    ReportTarget(String name) {
        this.name = name;
    }

    public static ReportTarget findReportTarget(String typeValue) {
        return Arrays.stream(ReportTarget.values()).
                filter(reportTarget -> reportTarget.name().equals(typeValue)).
                findAny().
                orElseThrow(() -> new ReportTargetNotFoundException(typeValue));
    }
}
