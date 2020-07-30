package com.saebyeok.saebyeok.exception;

public class IllegalSocialLoginException extends RuntimeException {
    public IllegalSocialLoginException(String snsToken) {
        super("유효하지 않은 토큰입니다: " + snsToken);
    }
}
