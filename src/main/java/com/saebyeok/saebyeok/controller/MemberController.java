package com.saebyeok.saebyeok.controller;

import com.saebyeok.saebyeok.dto.LoginRequest;
import com.saebyeok.saebyeok.dto.TokenResponse;
import com.saebyeok.saebyeok.exception.IllegalSocialLoginException;
import com.saebyeok.saebyeok.service.MemberService;
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
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        // TODO: 2020/07/28 소셜 로그인 API로 받은 토큰이 유효한지 확인 하는 로직 수정
        String snsToken = request.getSnsToken();
        if (!"This is a social login token".equals(snsToken)) {
            throw new IllegalSocialLoginException(snsToken);
        }

        // TODO: 2020/07/29 소셜 로그인 API로부터 사용자 정보 받아오기
        String accessToken = memberService.createToken(3L, snsToken);

        return ResponseEntity.ok(new TokenResponse(accessToken, "Bearer"));
    }
}
