package com.saebyeok.saebyeok.exception;

public class ArticleAnalysisMessageNotFoundException extends RuntimeException {
    public ArticleAnalysisMessageNotFoundException(Long articleEmotionId) {
        super(articleEmotionId + "에 해당하는 게시글 분석 메시지를 찾을 수 없습니다.");
    }
}
