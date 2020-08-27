package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<Void> createComment(Authentication authentication, @PathVariable Long articleId,
                                              @Valid @RequestBody CommentCreateRequest commentCreateRequest) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        Comment comment = commentService.createComment(member, commentCreateRequest);
        return ResponseEntity.
                created(URI.create("/articles/" + articleId + "/comments/" + comment.getId())).
                build();
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(Authentication authentication, @PathVariable Long articleId, @PathVariable Long commentId) throws IllegalAccessException {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));
        commentService.deleteComment(member, commentId);

        return ResponseEntity.
                noContent().build();
    }

}
