package com.saebyeok.saebyeok.security;

import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final String REQUEST_TOKEN_HEADER_NAME = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(REQUEST_TOKEN_HEADER_NAME);
        if (token != null && !token.equals("null")) {
            token = token.split(" ")[1];
        }
        if (isValidToken(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private boolean isValidToken(String token) {
        return Objects.nonNull(token)
                && jwtTokenProvider.isValidToken(token);
    }
}
