package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import com.saebyeok.saebyeok.service.RecommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class RecommentController {
    private final RecommentService recommentService;

    @PostMapping("/articles/{articleId}/comments/{commentId}/recomments")
    ResponseEntity<Long> createRecomment(@LoginMember Member member,
                                         @PathVariable Long articleId, @PathVariable Long commentId,
                                         @Valid @RequestBody RecommentCreateRequest recommentCreateRequest) {
        Long recommentId = recommentService.createRecomment(member, recommentCreateRequest);
        return ResponseEntity
                .created(URI.create("/articles/" + articleId + "/comments/" + commentId + "/recomments" + recommentId))
                .body(recommentId);
    }
}
