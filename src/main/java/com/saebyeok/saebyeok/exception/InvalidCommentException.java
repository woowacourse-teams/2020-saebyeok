package com.saebyeok.saebyeok.exception;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException() {
        super("올바르지 않은 댓글입니다!");
    }

    public InvalidCommentException(String message) {
        super(message);
    }
}
