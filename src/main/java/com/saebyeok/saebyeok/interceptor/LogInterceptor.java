package com.saebyeok.saebyeok.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j(topic = "DEFAULT_FILE_APPENDER")
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("{}를 호출하였습니다.", handler.toString());
        log.debug("METHOD : {}", request.getMethod());
        return true;
    }
}
