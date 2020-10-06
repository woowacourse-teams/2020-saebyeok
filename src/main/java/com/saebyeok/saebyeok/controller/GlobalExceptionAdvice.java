package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j(topic = "ERROR_FILE_LOGGER")
@RestControllerAdvice
public class GlobalExceptionAdvice {
    // TODO: 2020/07/20 exception을 어떻게 응답 처리할 것인지 방법을 고민해보자

    @ExceptionHandler({
            CommentNotFoundException.class,
            ArticleNotFoundException.class,
            EmotionNotFoundException.class,
            ReportCategoryNotFoundException.class,
            IllegalAccessException.class,
            DuplicateArticleLikeException.class,
            DuplicateCommentLikeException.class})
    public ResponseEntity<ExceptionResponse> validated(Exception exception) {
        return ResponseEntity.
                badRequest().
                body(new ExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> processValidationError(MethodArgumentNotValidException exception) {
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        return ResponseEntity.
                badRequest().
                body(new ExceptionResponse(errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(Exception exception) {
        log.error("에러 발생!", exception);

        return ResponseEntity.
                status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(new ExceptionResponse(exception.getMessage()));
    }
}

