package com.saebyeok.saebyeok.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * H2 데이터베이스의 스프링 보안 차단 / h2- 콘솔 (또는 application.yaml에 구성한 경로)
 * security 로그인 창 없애는 설정.
 * 프로덕션 환경에서는 사용하지 말라고 함!
 */

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/h2-console/**").permitAll();

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}
