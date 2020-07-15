package com.saebyeok.saebyeok.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        this("해당 댓글을 찾을 수 없습니다!");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
