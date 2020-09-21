package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.controller.resolver.LoginMember;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Recomment;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import com.saebyeok.saebyeok.service.RecommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class RecommentController {
    private final RecommentService recommentService;

    @PostMapping("/recomments")
    ResponseEntity<Long> createRecomment(@LoginMember Member member,
                                         @Valid @RequestBody RecommentCreateRequest recommentCreateRequest) {
        Recomment recomment = recommentService.createRecomment(member, recommentCreateRequest);
        return ResponseEntity
                .created(URI.create("/recomments" + recomment.getId()))
                .body(recomment.getId());
    }
}
