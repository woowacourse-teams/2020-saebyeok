package com.saebyeok.saebyeok.exception;

public class DuplicateArticleLikeException extends RuntimeException {
    public DuplicateArticleLikeException(Long member, Long articleId) {
        super("이미 공감한 게시물에 추가 공감을 할 수 없습니다. MemberId: " + member + "articleId" + articleId);
    }
}
