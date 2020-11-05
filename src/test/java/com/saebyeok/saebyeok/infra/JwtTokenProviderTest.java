package com.saebyeok.saebyeok.infra;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class JwtTokenProviderTest {

    public static final String INVALID_TOKEN = "invalid.token.123";
    public static final Date ONE_SECOND_AGO = new Date(new Date().getTime() - 1_000);
    public static final Date THOUSAND_SECOND_AGO = new Date(new Date().getTime() - 1_000_000);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Value("${security.jwt.token.secret-key}")
    String secretKey;

    @DisplayName("토큰화 할 정보 전달해서 토큰을 만들 수 있다.")
    @Test
    void createToken() {
        String subject = "social identification";
        String token = jwtTokenProvider.createToken(subject);

        assertThat(token).isNotBlank();
    }

    @DisplayName("토큰이 유효한지 검사할 수 있다.")
    @Test
    void validateToken() {
        String subject = "social identification";
        String token = jwtTokenProvider.createToken(subject);

        assertThat(jwtTokenProvider.isValidToken(token)).isTrue();
    }

    @DisplayName("토큰이 유효하지 않으면 false를 반환한다.")
    @Test
    void invalidTokenTest() {
        String expiredKey = Jwts.builder()
                .setClaims(Jwts.claims().setSubject("social identification"))
                .setIssuedAt(THOUSAND_SECOND_AGO)
                .setExpiration(ONE_SECOND_AGO)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes()))
                .compact();

        assertAll(
                () -> assertThat(jwtTokenProvider.isValidToken(INVALID_TOKEN)).isFalse(),
                () -> assertThat(jwtTokenProvider.isValidToken(expiredKey)).isFalse()
        );
    }
}
