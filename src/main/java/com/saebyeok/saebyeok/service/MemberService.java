package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Gender;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.domain.Role;
import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(Long id, String snsId) {
        // TODO: 2020/07/28 추후 소셜 로그인 API에서 회원 정보를 받고, 그 식별자가 새벽 서비스 DB에 있는지 확인하도록 수정
        Member memberFromSNS = new Member(null, 1991, Gender.FEMALE, LocalDateTime.now(), false, new ArrayList<>());

        Member member = memberRepository.findById(id)
                .orElseGet(() -> memberRepository.save(memberFromSNS));

        return jwtTokenProvider.createToken(snsId);
    }
}