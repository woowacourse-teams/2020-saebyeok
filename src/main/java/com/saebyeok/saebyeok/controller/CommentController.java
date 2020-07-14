package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.InvalidCommentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class CommentController {

    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long id, @RequestBody CommentCreateRequest commentRequest) {
        if (commentRequest.getContent().trim().length() < 1) {
            throw new InvalidCommentException("댓글의 최소 길이는 한 글자입니다!");
        }

        if (commentRequest.getContent().trim().length() > 140) {
            throw new InvalidCommentException("댓글의 최대 길이는 140자입니다!");
        }

        return ResponseEntity.
            created(URI.create("/articles/" + 1L)).
            body(1L);
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        return ResponseEntity.
            noContent().build();
    }

}
