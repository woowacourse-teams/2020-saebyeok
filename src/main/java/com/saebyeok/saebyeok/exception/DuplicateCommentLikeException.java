package com.saebyeok.saebyeok.exception;

public class DuplicateCommentLikeException extends RuntimeException {
    public DuplicateCommentLikeException(Long memberId, Long commentId) {
        super("이미 공감한 댓글에 추가 공감을 할 수 없습니다. MemberId: " + memberId + ", commentId: " + commentId);
    }
}
