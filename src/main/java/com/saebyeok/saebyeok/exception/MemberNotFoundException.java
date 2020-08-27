package com.saebyeok.saebyeok.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberId) {
        super(memberId + "에 해당하는 회원을 찾을 수 없습니다.");
    }
}
