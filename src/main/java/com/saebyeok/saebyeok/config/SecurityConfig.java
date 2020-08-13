package com.saebyeok.saebyeok.config;

import com.saebyeok.saebyeok.infra.JwtTokenProvider;
import com.saebyeok.saebyeok.security.JwtAuthenticationFilter;
import com.saebyeok.saebyeok.security.SuccessHandler;
import com.saebyeok.saebyeok.security.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final SuccessHandler successHandler;
    private final JwtTokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)
                .and()
                    .successHandler(successHandler)
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(tokenProvider),
                            OAuth2LoginAuthenticationFilter.class);
        //@formatter:on
    }
}