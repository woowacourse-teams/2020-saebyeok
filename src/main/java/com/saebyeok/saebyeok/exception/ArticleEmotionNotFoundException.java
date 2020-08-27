package com.saebyeok.saebyeok.exception;

public class ArticleEmotionNotFoundException extends RuntimeException {
    public ArticleEmotionNotFoundException(Long articleId) {
        super(articleId + "에 해당하는 게시글의 감정 대분류를 찾을 수 없습니다.");
    }
}