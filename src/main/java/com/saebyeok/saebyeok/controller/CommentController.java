package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long articleId,
                                              @RequestBody CommentCreateRequest commentCreateRequest) {
        commentService.createComment(commentCreateRequest);

        return ResponseEntity.
            created(URI.create("/articles/" + articleId)).
            build();
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.
            noContent().build();
    }

}
