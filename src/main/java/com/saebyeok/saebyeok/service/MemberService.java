package com.saebyeok.saebyeok.service;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public String createToken() {
        return "saebyeokToken";
    }
}