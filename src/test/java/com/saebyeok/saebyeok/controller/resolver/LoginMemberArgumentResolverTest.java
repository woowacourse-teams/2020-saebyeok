package com.saebyeok.saebyeok.controller.resolver;

import com.saebyeok.saebyeok.domain.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.assertj.core.api.Assertions.assertThat;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LoginMemberArgumentResolverTest {
    @Autowired
    private LoginMemberArgumentResolver resolver;

    @Mock
    private MethodParameter parameter;
    @Mock
    private ModelAndViewContainer mavContainer;
    @Mock
    private NativeWebRequest webRequest;
    @Mock
    private WebDataBinderFactory binderFactory;

    @Test
    void resolveArgumentTest() {
        Object member = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        assertThat((Member) member).isNotNull();
        assertThat(((Member) member).getOauthId()).isEqualTo("123456789");
    }
}