package com.saebyeok.saebyeok.acceptance;

import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@WithUserDetails("123456789")
@Sql({"/truncate.sql", "/emotion.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    public static final String API = "/api";
    public static String token = null;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    int port;

    public static RequestSpecification given() {
        return RestAssured.given().log().all();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        token = jwtTokenProvider.createToken("123456789");
    }
}