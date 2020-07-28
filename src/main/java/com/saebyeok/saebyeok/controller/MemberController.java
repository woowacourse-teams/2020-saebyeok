package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.TokenResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberController {

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody String snsToken) {
        return ResponseEntity.ok(new TokenResponse("accessToken", "Bearer"));
    }
}
