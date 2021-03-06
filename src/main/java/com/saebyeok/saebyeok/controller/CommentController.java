package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.dto.CommentResponse;
import com.saebyeok.saebyeok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@LoginMember Member member, @PathVariable Long articleId) {
        List<CommentResponse> commentResponses = commentService.getComment(member, articleId);
        return ResponseEntity.ok(commentResponses);
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Long> createComment(@LoginMember Member member, @PathVariable Long articleId,
                                              @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        Comment comment = commentService.createComment(member, commentCreateRequest);
        return ResponseEntity.
                created(URI.create("/articles/" + articleId + "/comments/" + comment.getId())).
                body(comment.getId());
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@LoginMember Member member, @PathVariable Long articleId, @PathVariable Long commentId) throws IllegalAccessException {
        commentService.deleteComment(member, commentId);

        return ResponseEntity.
                noContent().build();
    }

}
