package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("댓글 등록 요청을 받을 때, 댓글을 등록하고 댓글의 ID를 반환한다")
    @Test
    void createCommentTest() throws Exception {
        Member member = new Member();
        LocalDateTime now = LocalDateTime.now();
        Article article = new Article();

        Comment comment = Comment.builder().
            content("새벽 좋아요").
            member(member).
            nickname("시라소니").
            createdDate(now).
            article(article).
            isDeleted(false).
            build();

        String content = objectMapper.writeValueAsString(comment);

        given(commentService.createComment(any())).willReturn(comment);

        this.mockMvc.perform(post("/articles/" + 1L + "/comments").
            content(content).
            accept(MediaType.APPLICATION_JSON).
            contentType(MediaType.APPLICATION_JSON)).
            andExpect(status().isCreated()).
            andExpect(content().string("1")).
            andDo(print());
    }

    @DisplayName("댓글 삭제 요청을 받을 때, 댓글을 삭제한다")
    @Test
    void deleteCommentTest() throws Exception {
        this.mockMvc.perform(delete("/articles/" + 1L + "/comments/" + 1L)).
            andExpect(status().isNoContent()).
            andDo(print());
    }

}
