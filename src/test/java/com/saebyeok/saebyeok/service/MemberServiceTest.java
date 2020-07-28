package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("소셜 로그인 API로 받은 새로운 회원 정보로 토큰을 요청하면 회원을 등록하고 토큰을 돌려받는다.")
    @Test
    void createTokenWithNewMember() {
        when(memberRepository.findById(3L)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(any(Member.class));

        String accessToken = memberService.createToken(3L);

        assertThat(accessToken).isEqualTo("saebyeokToken");
        verify(memberRepository).save(any(Member.class));
    }
}