package com.saebyeok.saebyeok.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super(commentId + "에 해당하는 댓글을 찾을 수 없습니다!");
    }
}
