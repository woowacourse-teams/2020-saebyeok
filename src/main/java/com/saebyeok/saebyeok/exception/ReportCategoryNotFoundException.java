package com.saebyeok.saebyeok.exception;

public class ReportCategoryNotFoundException extends RuntimeException {
    public ReportCategoryNotFoundException(Long reportCategoryId) {
        super(reportCategoryId + "에 해당하는 신고 분류를 찾을 수 없습니다.");
    }
}
