package com.saebyeok.saebyeok.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = JwtTokenProvider.class)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰화 할 정보 전달해서 토큰을 만들 수 있다.")
    @Test
    void createToken() {
        String subject = "social identification";

        String token = jwtTokenProvider.createToken(subject);

        assertThat(token).isNotBlank();
    }
}