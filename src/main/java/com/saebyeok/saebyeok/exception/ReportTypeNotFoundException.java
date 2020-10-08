package com.saebyeok.saebyeok.exception;

public class ReportTypeNotFoundException extends RuntimeException {
    public ReportTypeNotFoundException(String name) {
        super(name + "에 해당하는 신고 타입을 찾을 수 없습니다.");
    }
}
