package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }

    public Member findByOauthId(String oauthId) {
        return memberRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UsernameNotFoundException(oauthId + "에 해당하는 사용자를 찾을 수 없습니다."));
    }
}
