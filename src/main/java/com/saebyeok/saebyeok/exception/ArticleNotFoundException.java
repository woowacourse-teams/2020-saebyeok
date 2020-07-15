package com.saebyeok.saebyeok.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(String articleId) {
        super(articleId + "에 해당하는 게시글을 찾을 수 없습니다.");
    }
}
