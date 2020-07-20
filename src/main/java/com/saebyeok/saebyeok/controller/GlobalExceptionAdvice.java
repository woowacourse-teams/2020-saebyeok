package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.InvalidLengthCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {
    // TODO: 2020/07/20 exception을 어떻게 응답 처리할 것인지 방법을 고민해보자

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidLengthCommentException.class, CommentNotFoundException.class})
    public ExceptionResponse validated(RuntimeException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ExceptionResponse> articleNotFoundHandler(ArticleNotFoundException e) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(e.getMessage()));
    }
}

