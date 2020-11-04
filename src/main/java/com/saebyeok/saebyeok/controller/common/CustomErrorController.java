package com.saebyeok.saebyeok.controller.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class CustomErrorController implements ErrorController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/error")
    public String redirectRoot() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
