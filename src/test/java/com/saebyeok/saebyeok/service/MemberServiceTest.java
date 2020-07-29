package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.saebyeok.saebyeok.acceptance.MemberAcceptanceTest.SNS_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, jwtTokenProvider);
    }

    @DisplayName("소셜 로그인 API로 받은 새로운 회원 정보로 토큰을 요청하면 회원을 등록하고 토큰을 돌려받는다.")
    @Test
    void createTokenWithNewMember() {
        when(memberRepository.findById(3L)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenReturn(new Member()); // TODO: 2020/07/29 thenReturn에 any(Member.class)를 쓰면 에러가난다..! k
        when(jwtTokenProvider.createToken(any())).thenReturn("saebyeokToken");

        String accessToken = memberService.createToken(3L, SNS_TOKEN);

        verify(memberRepository).save(any(Member.class));
        assertThat(accessToken).isEqualTo("saebyeokToken");
    }
}