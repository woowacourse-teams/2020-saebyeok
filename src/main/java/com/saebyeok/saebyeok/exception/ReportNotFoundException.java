package com.saebyeok.saebyeok.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(Long reportCategoryId) {
        super(reportCategoryId + "에 해당하는 신고 이력을 찾을 수 없습니다.");
    }
}
