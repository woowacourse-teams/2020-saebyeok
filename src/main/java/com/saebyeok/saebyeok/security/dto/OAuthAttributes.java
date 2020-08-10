package com.saebyeok.saebyeok.security.dto;

import com.saebyeok.saebyeok.domain.Gender;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String email;
    private Gender gender;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String email, Gender gender) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.gender = gender;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .gender(Gender.findGender((String) attributes.get("gender")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .gender(Gender.findGender((String) response.get("gender")))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .role(Role.USER)
                .gender(gender)
                .build();
    }
}