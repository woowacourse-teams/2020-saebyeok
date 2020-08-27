package com.saebyeok.saebyeok.exception;

public class SubEmotionNotFoundException extends RuntimeException {
    public SubEmotionNotFoundException(Long emotionId) {
        super(emotionId + "에 해당하는 감정 소분류를 찾을 수 없습니다.");
    }
}
