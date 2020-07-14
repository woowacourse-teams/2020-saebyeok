package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CommentController {

    @PostMapping("/articles/{id}/comments")
    public ResponseEntity<Long> createComment(@PathVariable Long id, @RequestBody CommentCreateRequest commentRequest) {
        return ResponseEntity
            .created(URI.create("/articles/" + 1L))
            .body(1L);
    }

}
