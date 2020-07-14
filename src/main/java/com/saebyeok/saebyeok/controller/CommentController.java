package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // TODO: 2020/07/14 리턴할 body값에 comment.getId값을 넣어줘야 함 
    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long id,
                                              @RequestBody CommentCreateRequest commentCreateRequest) {
        Comment comment = commentService.createComment(commentCreateRequest);

        return ResponseEntity.
            created(URI.create("/articles/" + id)).
            body(1L);
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(articleId, commentId);

        return ResponseEntity.
            noContent().build();
    }

}
