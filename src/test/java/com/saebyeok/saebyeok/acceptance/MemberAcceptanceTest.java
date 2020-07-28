package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.dto.TokenResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberAcceptanceTest {
    public static final String SNS_TOKEN = "This is an access token";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Scenario: 회원 가입을 하고 로그인 할 수 있다.
     * <p>
     * given 소셜 로그인 결과로 유효한 토큰을 가지고 있다.
     * when 회원 가입 요청을 한다.
     * then 회원이 추가 되고 로그인이 되어 새벽 전 토큰을 리턴 받는다.
     * <p>
     * given 유효하지 않은 소셜 로그 토큰을 가지고 있다.
     * when 회원 가입 요청을 한다.
     * then 에러 메시지를 리턴 받는다.
     * <p>
     * given 소셜 로그인 결과로 유효한 토큰을 가지고 있고 회원 가입을 한 상태이다.
     * when 회원 가입 요청을 한다.
     * then 로그인이 되 새벽 전용 토큰을 리턴 받는다.
     **/

    @DisplayName("유효한 소셜 로그인 토큰으로 회원 가입, 로그인을 할 수 있다.")
    @Test
    void manageMember() {
        //given, when
        TokenResponse saebyeokToken = loginNewMember(SNS_TOKEN);

        //then
        assertThat(saebyeokToken).isNotNull();
        assertThat(saebyeokToken.getTokenType()).isEqualTo("Bearer");
        assertThat(saebyeokToken.getAccessToken()).isNotBlank();
    }

    private TokenResponse loginNewMember(String accessToken) {
        //@formatter:off
        return given().
                body(accessToken).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/login").
                then().
                log().all().
                extract().as(TokenResponse.class);
        //@formatter:on
    }
}
