package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.domain.ArticleLike;
import com.saebyeok.saebyeok.domain.CommentLike;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikeController {

    private final MemberRepository memberRepository;
    private final LikeService likeService;

    @PostMapping("/likes/article/{articleId}")
    public ResponseEntity<Void> likeArticle(Authentication authentication, @PathVariable Long articleId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        ArticleLike articleLike = likeService.likeArticle(member, articleId);

        return ResponseEntity
                .created(URI.create("/likes/article/" + articleId + "/" + articleLike.getId()))
                .build();
    }

    @DeleteMapping("/likes/article/{articleId}")
    public ResponseEntity<Void> unlikeArticle(Authentication authentication, @PathVariable Long articleId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        likeService.unlikeArticle(member, articleId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes/comment/{commentId}")
    public ResponseEntity<Void> likeComment(Authentication authentication, @PathVariable Long commentId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        CommentLike commentLike = likeService.likeComment(member, commentId);

        return ResponseEntity
                .created(URI.create("/likes/comment/" + commentId + "/" + commentLike.getId()))
                .build();
    }

    @DeleteMapping("/likes/comment/{commentId}")
    public ResponseEntity<Void> unlikeComment(Authentication authentication, @PathVariable Long commentId) {
        // TODO: 20. 8. 11. 커스텀 어노테이션으로 리팩토링
        User user = (User) authentication.getPrincipal();
        Member member = memberRepository.findById(user.getId())
                .orElseThrow(() -> new MemberNotFoundException(user.getId()));

        likeService.unlikeComment(member, commentId);

        return ResponseEntity.noContent().build();
    }
}
