package com.saebyeok.saebyeok.exception;

import static com.saebyeok.saebyeok.domain.Comment.MAX_LENGTH;
import static com.saebyeok.saebyeok.domain.Comment.MIN_LENGTH;

public class InvalidLengthCommentException extends RuntimeException {
    public InvalidLengthCommentException(int length) {
        // TODO: 2020/07/20 메시지 출력 방법 고민해보자 
        super(String.format("글자수가 %d이므로 올바르지 않은 댓글입니다! (최소 %d자, 최대 %d자)", length, MIN_LENGTH, MAX_LENGTH));
    }
}
