package com.saebyeok.saebyeok.security.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.MemberRepository;
import com.saebyeok.saebyeok.security.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findByOauthId(id).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return User.builder()
                .id(member.getId())
                .oauthId(member.getOauthId())
                .role(member.getRole())
                .build();
    }
}