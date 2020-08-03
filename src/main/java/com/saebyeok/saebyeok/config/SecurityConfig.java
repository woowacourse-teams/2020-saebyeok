package com.saebyeok.saebyeok.config;

import com.saebyeok.saebyeok.security.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * H2 데이터베이스의 스프링 보안 차단 / h2- 콘솔 (또는 application.yaml에 구성한 경로)
 * security 로그인 창 없애는 설정.
 * 프로덕션 환경에서는 사용하지 말라고 함!
 */

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http.csrf().disable()
            .headers().frameOptions().disable()
            .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/css/**", "/images/**", "/js/**", "/profile").permitAll()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/sign-in")
                    .permitAll()
                    .defaultSuccessUrl("/feed")
            .and()
                .logout()
                    .logoutSuccessUrl("/")
            .and()
                .oauth2Login()
//                .loginPage("/login")
//                .defaultSuccessUrl("/")
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService);
        //@formatter:on
    }
}
