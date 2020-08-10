package com.saebyeok.saebyeok.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender findGender(String genderValue) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.name().startsWith(genderValue))
                .findAny().orElseThrow(() -> new RuntimeException());
    }
}
