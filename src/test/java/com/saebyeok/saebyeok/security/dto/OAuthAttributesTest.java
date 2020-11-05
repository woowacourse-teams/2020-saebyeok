package com.saebyeok.saebyeok.security.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OAuthAttributesTest {
    @Test
    @DisplayName("알맞은 자료 해독 전략을 불러오는 테스트")
    void OfTest() {
        Map<String, Object> attributes = new HashMap<>();
        Map<String, Object> naverAttributes = new HashMap<>();
        attributes.put("name", "Andy H. Jung");
        attributes.put("id", "123456789");
        naverAttributes.put("response", attributes);
        assertThat(OAuthAttributes.of("naver", "id", naverAttributes))
                .isInstanceOf(OAuthAttributes.class);
        assertThat(OAuthAttributes.of("google", "id", attributes))
                .isInstanceOf(OAuthAttributes.class);
    }

    @Test
    @DisplayName("잘못된 registrationId가 올 경우 예외 처리")
    void wrongRegistrationIdTest() {
        assertThatThrownBy(() -> OAuthAttributes.of("daum", "id", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바르지 않은 소셜 로그인 방법");

    }
}
