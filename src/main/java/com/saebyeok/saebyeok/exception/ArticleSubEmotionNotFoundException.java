package com.saebyeok.saebyeok.exception;

public class ArticleSubEmotionNotFoundException extends RuntimeException {
    public ArticleSubEmotionNotFoundException(Long articleId) {
        super(articleId + "에 해당하는 게시글의 감정 소분류를 찾을 수 없습니다.");
    }
}
