package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Comment;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.saebyeok.saebyeok.domain.CommentTest.TEST_CONTENT;
import static com.saebyeok.saebyeok.domain.CommentTest.TEST_NICKNAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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

    @MockBean
    private CommentService commentService;

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
        Comment comment = Comment.builder().
                content(TEST_CONTENT).
                nickname(TEST_NICKNAME).
                isDeleted(false).
                build();

        String content = objectMapper.writeValueAsString(comment);

        given(commentService.createComment(any(), any())).willReturn(comment);

        this.mockMvc.perform(post(API + "/articles/" + ARTICLE_ID + "/comments").
                content(content).
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
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
