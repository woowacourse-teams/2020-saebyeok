package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.CommentCreateRequest;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.saebyeok.saebyeok.domain.CommentTest.TEST_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
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
    private static final Long INVALID_ARTICLE_ID = 100L;

    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    // TODO: 2020/07/20 controllerTest가 더 생기면 objectMapper 공통 사용 고려하기
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("댓글 등록 요청을 받을 때, 댓글을 등록 후 댓글 id를 반환한다.")
    @Test
    void createCommentTest() throws Exception {
        when(commentService.createComment(any(), any())).thenReturn(new Comment(COMMENT_ID, null, null, null, null, null, null, null, null));

        CommentCreateRequest request = new CommentCreateRequest(TEST_CONTENT, ARTICLE_ID, null);
        String content = objectMapper.writeValueAsString(request);

        MvcResult mvcResult = this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(content).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print()).
                andReturn();

        Long savedCommentId = Long.parseLong(mvcResult.getResponse().getContentAsString());

        assertThat(savedCommentId).isEqualTo(COMMENT_ID);

    }

    @DisplayName("예외 테스트: 최소 길이보다 짧은 댓글 등록 메서드를 호출했을 때, 예외가 발생한다")
    @Test
    void createUnderLengthCommentTest() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest("", ARTICLE_ID, null);
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

        CommentCreateRequest request = new CommentCreateRequest(content, ARTICLE_ID, null);
        String requestAsString = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }

    @DisplayName("예외 테스트: 함께 전달된 articleId가 존재하지 않을 경우, 예외가 발생한다")
    @Test
    void notExistArticleIdTest() throws Exception {
        when(commentService.createComment(any(), any())).thenThrow(ArticleNotFoundException.class);

        CommentCreateRequest request = new CommentCreateRequest(TEST_CONTENT, INVALID_ARTICLE_ID, null);
        String requestAsString = objectMapper.writeValueAsString(request);

        this.mockMvc.perform(post(API + "/articles/" + INVALID_ARTICLE_ID + "/comments").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
    }


    @DisplayName("댓글 삭제 요청을 받을 때, 댓글을 삭제한다")
    @Test
    void deleteCommentTest() throws Exception {
        doNothing().when(commentService).deleteComment(any(Member.class), anyLong());

        this.mockMvc.perform(delete(API + "/articles/" + ARTICLE_ID + "/comments/" + COMMENT_ID)).
                andExpect(status().isNoContent()).
                andDo(print());
    }
}
