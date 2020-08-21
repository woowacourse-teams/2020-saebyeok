package com.saebyeok.saebyeok.util;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class NicknameGenerator {
    public String generate(Member member, Article article) {
        return "TEST_NICKNAME";
    }
}
