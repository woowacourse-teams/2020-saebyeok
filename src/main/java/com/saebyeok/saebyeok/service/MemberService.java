package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
                .orElseThrow(() -> new MemberNotFoundException(oauthId));
    }

    public Member signIn(Member unconfirmedMember) {
        return memberRepository.findByOauthId(unconfirmedMember.getOauthId())
                .orElseGet(() -> signUp(unconfirmedMember));
    }

    public void deactivate(Member member) {
        member.deactivate();
    }

    private Member signUp(Member newMember) {
        return memberRepository.save(newMember);
    }
}
