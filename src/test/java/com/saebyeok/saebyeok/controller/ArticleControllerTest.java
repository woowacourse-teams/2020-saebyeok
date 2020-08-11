package com.saebyeok.saebyeok.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.dto.ArticleCreateRequest;
import com.saebyeok.saebyeok.dto.ArticleResponse;
import com.saebyeok.saebyeok.exception.ArticleNotFoundException;
import com.saebyeok.saebyeok.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = {ArticleController.class})
class ArticleControllerTest {
    private static final String API = "/api";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TEST_CONTENT = "내용";
    private static final String TEST_EMOTION = "기쁨";
    private static final Boolean TEST_IS_MINE = false;
    private static final Boolean TEST_IS_COMMENT_ALLOWED = true;
    private static final Long TEST_ID = 1L;
    private static final Long INVALID_ARTICLE_ID = 2L;
    private static final Integer TEST_PAGE_NUMBER = 0;
    private static final Integer TEST_PAGE_SIZE = 10;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    private ArticleResponse articleResponse;

    @BeforeEach
    void setUp() {
        articleResponse = new ArticleResponse(TEST_ID, TEST_CONTENT, LocalDateTime.now(), TEST_EMOTION, TEST_IS_COMMENT_ALLOWED, TEST_IS_MINE, null);
    }

    @DisplayName("'/articles'로 get 요청을 보내면 글 목록 리스트를 받는다")
    @Test
    void getArticlesTest() throws Exception {
        when(articleService.getArticles(any(Member.class), TEST_PAGE_NUMBER, TEST_PAGE_SIZE)).thenReturn(Arrays.asList(articleResponse));

        this.mockMvc.perform(get(API + "/articles?page=" + TEST_PAGE_NUMBER + "&size=" + TEST_PAGE_SIZE).
                accept(MediaType.APPLICATION_JSON_VALUE)).
                andExpect(jsonPath("$", hasSize(1))).
                andExpect(jsonPath("$[0].content").value(TEST_CONTENT));
    }

    @DisplayName("'/articles'로 post 요청을 보내면 글을 생성한다")
    @Test
    void createArticleTest() throws Exception {
        ArticleCreateRequest request = new ArticleCreateRequest(TEST_CONTENT, TEST_EMOTION, TEST_IS_COMMENT_ALLOWED);
        String requestAsString = OBJECT_MAPPER.writeValueAsString(request);

        when(articleService.createArticle(any(Member.class), any(ArticleCreateRequest.class))).thenReturn(request.toArticle());

        this.mockMvc.perform(post(API + "/articles").
                content(requestAsString).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @DisplayName("ID로 개별 글 조회를 요청하면 해당 글을 전달 받는다")
    @Test
    void readArticleTest() throws Exception {
        when(articleService.readArticle(any(Member.class), TEST_ID)).thenReturn(articleResponse);

        this.mockMvc.perform(get(API + "/articles/" + TEST_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").value(TEST_ID)).
                andExpect(jsonPath("$.content").value(TEST_CONTENT)).
                andExpect(jsonPath("$.emotion").value(TEST_EMOTION)).
                andExpect(jsonPath("$.isCommentAllowed").value(TEST_IS_COMMENT_ALLOWED));
    }

    @DisplayName("예외 테스트: 없는 ID의 글 조회를 요청하면 ArticleNotFoundException이 발생한다")
    @Test
    void readArticleExceptionTest() throws Exception {
        when(articleService.readArticle(any(Member.class), INVALID_ARTICLE_ID))
                .thenThrow(ArticleNotFoundException.class);

        this.mockMvc.perform(get(API + "/articles/" + INVALID_ARTICLE_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }

    @DisplayName("특정 ID의 글 삭제를 요청하면 해당 글을 삭제한다")
    @Test
    void deleteArticleTest() throws Exception {
        doNothing().when(articleService).deleteArticle(any(Member.class), TEST_ID);

        this.mockMvc.perform(delete(API + "/articles/" + TEST_ID)).
                andExpect(status().isNoContent());
    }

    @DisplayName("예외 테스트: 없는 ID의 글 삭제를 요청하면 ArticleNotFoundException이 발생한다")
    @Test
    void deleteArticleExceptionTest() throws Exception {
        doThrow(new ArticleNotFoundException(INVALID_ARTICLE_ID))
                .when(articleService).deleteArticle(any(Member.class), INVALID_ARTICLE_ID);

        this.mockMvc.perform(delete(API + "/articles/" + INVALID_ARTICLE_ID).
                contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());
    }
}
