package com.saebyeok.saebyeok.controller.resolver;

import com.saebyeok.saebyeok.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

    @Mock
    private Method method;

    @WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
    @DisplayName("로그인한 회원이 @LoginMember가 붙은 자원을 요청하면 Member를 리턴한다.")
    @Test
    void resolveArgumentTest() {
        when(parameter.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("anyRequestMethod");

        Object member = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        assertThat((Member) member).isNotNull();
        assertThat(((Member) member).getOauthId()).isEqualTo("123456789");
    }

    @WithAnonymousUser
    @DisplayName("비회원이 피드 혹은 글 디테일 페이지에 접근하면 빈 Member를 리턴한다.")
    @ValueSource(strings = {"getArticles", "readArticle"})
    @ParameterizedTest
    void anonymousResolverArgumentTest(String methodName) {
        when(parameter.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);

        Object member = resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);

        assertThat((Member) member).isNotNull();
    }
}