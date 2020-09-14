package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(memberRepository);
    }

    @DisplayName("id로 Member 조회 메서드를 실행하면 Member를 조회한다")
    @Test
    void findByIdTest() {
        Long validId = 1L;
        when(memberRepository.findById(validId)).thenReturn(Optional.of(new Member()));

        memberService.findById(validId);

        verify(memberRepository).findById(validId);
    }

    @DisplayName("예외 테스트: 유효하지 않은 아이디로 Member를 요청하면 예외가 발생한다")
    @Test
    void findByIdExceptionTest() {
        Long invalidId = 100L;
        when(memberRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById(invalidId))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage(invalidId + "에 해당하는 회원을 찾을 수 없습니다.");
    }
}