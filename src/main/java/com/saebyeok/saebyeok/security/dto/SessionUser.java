package com.saebyeok.saebyeok.security.dto;

import com.saebyeok.saebyeok.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {//여기엔 인증된 사용자 정보만 필요.
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}