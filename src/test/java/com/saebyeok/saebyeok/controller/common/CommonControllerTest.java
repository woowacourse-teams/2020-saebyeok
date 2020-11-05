package com.saebyeok.saebyeok.controller.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CommonControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("현재 active된 profile의 set 값 확인")
    @Test
    public void profileTest() {
        String profile = this.restTemplate.getForObject("/profile", String.class);

        assertThat(profile).isEqualTo("set1");
    }
}
