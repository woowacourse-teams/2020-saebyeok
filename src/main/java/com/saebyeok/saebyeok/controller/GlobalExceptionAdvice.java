package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.EmotionNotFoundException;
import com.saebyeok.saebyeok.exception.InvalidLengthCommentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "ERROR_FILE_LOGGER")
@RestControllerAdvice
public class GlobalExceptionAdvice {
    // TODO: 2020/07/20 exception을 어떻게 응답 처리할 것인지 방법을 고민해보자

    @ExceptionHandler({
            InvalidLengthCommentException.class,
            CommentNotFoundException.class,
            ArticleNotFoundException.class,
            EmotionNotFoundException.class,
            IllegalAccessException.class})
    public ResponseEntity<ExceptionResponse> validated(Exception exception) {
        return ResponseEntity.
                badRequest().
                body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exception) {
        log.error("에러 발생!", exception);

        return ResponseEntity.
            status(HttpStatus.INTERNAL_SERVER_ERROR).
            body(new ExceptionResponse(exception.getMessage()));
    }
}

