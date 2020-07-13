package com.saebyeok.saebyeok.domain;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String name;

    Gender(String name) {
        this.name = name;
    }
}
