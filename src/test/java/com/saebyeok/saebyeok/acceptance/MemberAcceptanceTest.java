package com.saebyeok.saebyeok.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.dto.ExceptionResponse;
import com.saebyeok.saebyeok.dto.TokenResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberAcceptanceTest {
    public static final String SNS_TOKEN = "This is a social login token";
    public static final String INVALID_SNS_TOKEN = "This is not a social login token";
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * Scenario: 유효한 소셜 로그인 토큰으로 로그인을 할 수 있다.
     * <p>
     * given 소셜 로그인 결과로 유효한 토큰을 가지고 있다.
     * when 로그인을 요청한다.
     * then 로그인이 되어 새벽 전 토큰을 리턴 받는다.
     * <p>
     * given 유효하지 않은 소셜 로그인 토큰을 가지고 있다.
     * when 로그인을 요청한다.
     * then 에러 메시지를 리턴 받는다.
     **/

    @DisplayName("유효한 소셜 로그인 토큰으로 회원 가입, 로그인을 할 수 있다.")
    @Test
    void manageMember() throws JsonProcessingException {
        //given, when: 유효한 소셜 로그인 토큰으로 로그인 요청을 한다.
        TokenResponse saebyeokToken = loginNewMember(SNS_TOKEN);

        //then: 회원 가입을 하고 새벽 전용 토큰을 받는다.
        assertThat(saebyeokToken).isNotNull();
        assertThat(saebyeokToken.getTokenType()).isEqualTo("Bearer");
        assertThat(saebyeokToken.getAccessToken()).isNotBlank();

        //given, when: 유효하지 않은 소셜 로그인 토큰으로 로그인 요청을 한다.
        ExceptionResponse error = loginWithInvalidToken(INVALID_SNS_TOKEN);

        //then: 에러 메시지를 받는다.
        assertThat(error.getErrorMessage()).contains("유효하지 않은 토큰입니다: " + INVALID_SNS_TOKEN);
    }

    private TokenResponse loginNewMember(String accessToken) throws JsonProcessingException {
        HashMap<String, String> request = new HashMap<>();
        request.put("snsToken", accessToken);
        String content = OBJECT_MAPPER.writeValueAsString(request);

        //@formatter:off
        return given().
                body(content).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/login").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(TokenResponse.class);
        //@formatter:on
    }

    private ExceptionResponse loginWithInvalidToken(String accessToken) throws JsonProcessingException {
        HashMap<String, String> request = new HashMap<>();
        request.put("snsToken", accessToken);
        String content = OBJECT_MAPPER.writeValueAsString(request);

        //@formatter:off
        return given().
                body(content).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/login").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(ExceptionResponse.class);
        //@formatter:on
    }
}
