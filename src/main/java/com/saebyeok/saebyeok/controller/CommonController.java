package com.saebyeok.saebyeok.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class CommonController implements ErrorController {
    private final Environment env;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/error")
    public String redirectRoot() {
        return "index.html";
    }

    @ResponseBody
    @GetMapping("/profile")
    public String getProfile() {
        return Arrays.stream(env.getActiveProfiles())
                .filter(it -> it.startsWith("set"))
                .findFirst()
                .orElse("set1");
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}


