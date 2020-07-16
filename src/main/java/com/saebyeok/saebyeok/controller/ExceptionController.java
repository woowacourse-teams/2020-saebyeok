package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.exception.CommentNotFoundException;
import com.saebyeok.saebyeok.exception.InvalidCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidCommentException.class, CommentNotFoundException.class})
    public ExceptionResponse validated(RuntimeException e) {
        return new ExceptionResponse(e.getMessage());
    }

}

