package com.saebyeok.saebyeok.security.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String id) {
        Member member = memberService.findByOauthId(id);
        return User.builder()
                .id(member.getId())
                .oauthId(member.getOauthId())
                .role(member.getRole())
                .build();
    }
}