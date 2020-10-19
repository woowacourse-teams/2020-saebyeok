package com.saebyeok.saebyeok.controller.resolver;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.security.User;
import com.saebyeok.saebyeok.service.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String methodName = Objects.requireNonNull(parameter.getMethod()).getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken &&
                ("getArticles".equals(methodName) || "readArticle".equals(methodName))) {  // 비회원이 피드 혹은 글 디테일 요청
            return new Member();
        } else {
            User user = (User) authentication.getPrincipal();
            return memberService.findById(user.getId());
        }
    }
}
