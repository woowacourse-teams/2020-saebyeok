package com.saebyeok.saebyeok.security;

import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@AllArgsConstructor
@Component
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> userInfo = ((DefaultOAuth2User) (authentication.getPrincipal())).getAttributes();
        String id = (String) userInfo.get("id");

        String accessToken = jwtTokenProvider.createToken(id);

        response.sendRedirect("/auth?token=bearer " + accessToken);
    }
}