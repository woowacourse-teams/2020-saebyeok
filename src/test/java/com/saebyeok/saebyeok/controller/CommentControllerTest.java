package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithUserDetails(userDetailsServiceBeanName = "userService", value = "123456789")
@SpringBootTest
class CommentControllerTest {
    private static final String API = "/api";
    private static final Long COMMENT_ID = 1L;
    private static final Long ARTICLE_ID = 1L;
    private static final String TEST_CONTENT = "내용";

    private MockMvc mockMvc;

    // TODO: 2020/07/20 controllerTest가 더 생기면 objectMapper 공통 사용 고려하기
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("댓글 등록 요청을 받을 때, 댓글을 등록 후 정상적으로 응답한다")
    @Test
    void createCommentTest() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest(TEST_CONTENT, ARTICLE_ID);
        String content = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(content).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 최소 길이보다 짧은 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createUnderLengthCommentTest() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest("", ARTICLE_ID);
        String requestAsString = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 최대 길이보다 긴 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createOverLengthCommentTest() throws Exception {
        String content = "나만 잘되게 해주세요(강보라 지음·인물과사상사)=자존감이 높은 사람과 ‘관종’의 차이는 무엇일까? " +
                "‘취향 존중’이 유행하고, ‘오이를 싫어하는 사람들의 모임’이 생기는 이유는 뭘까? 이 시대 새로운 지위를 차지하고 있는 ‘개인’에 관한 탐구 보고서. " +
                "1만4000원.\n";

        CommentCreateRequest request = new CommentCreateRequest(content, ARTICLE_ID);
        String requestAsString = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }


    @DisplayName("댓글 삭제 요청을 받을 때, 댓글을 삭제한다")
    @Test
    void deleteCommentTest() throws Exception {
        this.mockMvc.perform(delete(API + "/articles/" + ARTICLE_ID + "/comments/" + COMMENT_ID)).
                andExpect(status().isNoContent()).
                andDo(print());
    }
}
