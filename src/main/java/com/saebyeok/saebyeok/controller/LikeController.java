package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.ArticleLike;
import com.saebyeok.saebyeok.domain.CommentLike;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/likes/article/{articleId}")
    public ResponseEntity<Void> likeArticle(@LoginMember Member member, @PathVariable Long articleId) {
        ArticleLike articleLike = likeService.likeArticle(member, articleId);
        return ResponseEntity
                .created(URI.create("/likes/article/" + articleId + "/article_like/" + articleLike.getId()))
                .build();
    }

    @DeleteMapping("/likes/article/{articleId}")
    public ResponseEntity<Void> unlikeArticle(@LoginMember Member member, @PathVariable Long articleId) {
        likeService.unlikeArticle(member, articleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes/comment/{commentId}")
    public ResponseEntity<Void> likeComment(@LoginMember Member member, @PathVariable Long commentId) {
        CommentLike commentLike = likeService.likeComment(member, commentId);
        return ResponseEntity
                .created(URI.create("/likes/comment/" + commentId + "/comment_like" + commentLike.getId()))
                .build();
    }

    @DeleteMapping("/likes/comment/{commentId}")
    public ResponseEntity<Void> unlikeComment(@LoginMember Member member, @PathVariable Long commentId) {
        likeService.unlikeComment(member, commentId);
        return ResponseEntity.noContent().build();
    }
}
