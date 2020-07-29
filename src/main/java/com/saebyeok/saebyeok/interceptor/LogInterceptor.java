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
        // TODO: 2020/07/28 멤버 도메인이 완성되면 멤버 정보도 로그로 찍기!
        log.info("{} {}", request.getMethod(), request.getRequestURL());

        return true;
    }
}
