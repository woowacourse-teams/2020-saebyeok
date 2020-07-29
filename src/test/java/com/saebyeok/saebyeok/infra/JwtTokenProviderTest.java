package com.saebyeok.saebyeok.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret", 3600000);
    }

    @DisplayName("토큰화 할 정보 전달해서 토근을 만들 수 있다.")
    @Test
    void createToken() {
        String subject = "social identification";

        String token = jwtTokenProvider.createToken(subject);

        assertThat(token).isNotBlank();
    }
}